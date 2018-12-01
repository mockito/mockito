/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.configuration.injection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.mockito.InjectUnsafe;
import org.mockito.InjectUnsafe.UnsafeFieldModifier;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TerminalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.collections.ListUtil.Predicate;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;

import static org.mockito.InjectUnsafe.UnsafeFieldModifier.STATIC;
import static org.mockito.internal.exceptions.Reporter.cannotInitializeForInjectMocksAnnotation;
import static org.mockito.internal.exceptions.Reporter.fieldInitialisationThrewException;
import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;
import static org.mockito.internal.util.reflection.SuperTypesLastSorter.sortSuperTypesLast;

/**
 * Inject mocks using first setters then fields, if no setters available.
 *
 * <p>
 * <u>Algorithm :<br></u>
 * for each field annotated by @InjectMocks
 *   <ul>
 *   <li>initialize field annotated by @InjectMocks
 *   <li>for each fields of a class in @InjectMocks type hierarchy
 *     <ul>
 *     <li>make a copy of mock candidates
 *     <li>order fields from sub-type to super-type, then by field name
 *     <li>for the list of fields in a class try two passes of :
 *         <ul>
 *             <li>find mock candidate by type
 *             <li>if more than <b>*one*</b> candidate find mock candidate on name
 *             <li>if one mock candidate then
 *                 <ul>
 *                     <li>set mock by property setter if possible
 *                     <li>else set mock by field injection
 *                 </ul>
 *             <li>remove mock from mocks copy (mocks are just injected once in a class)
 *             <li>remove injected field from list of class fields
 *         </ul>
 *     <li>else don't fail, user will then provide dependencies
 *     </ul>
 *   </ul>
 * </p>
 *
 * <p>
 * <u>Note:</u> If the field needing injection is not initialized, the strategy tries
 * to create one using a no-arg constructor of the field type.
 * </p>
 * <p>
 * <u>Note:</u> Injection behavior can be altered using {@link InjectUnsafe}.
 * </p>
 */
public class PropertyAndSetterInjection extends MockInjectionStrategy {

    private final MockCandidateFilter mockCandidateFilter =
            new TypeBasedCandidateFilter(
                    new NameBasedCandidateFilter(
                            new TerminalMockCandidateFilter()));


    private static final Predicate<Field> ACCEPT_ANY_STATIC = new Predicate<Field>() {
        @Override
        public boolean test(Field object) {
            return Modifier.isStatic(object.getModifiers());
        }
    };

    private static final Predicate<Field> ACCEPT_ANY_FINAL = new Predicate<Field>() {
        @Override
        public boolean test(Field object) {
            return Modifier.isFinal(object.getModifiers());
        }
    };

    private static final Predicate<Field> ACCEPT_INSTANCE_FIELD = ACCEPT_ANY_FINAL.negate().and(ACCEPT_ANY_STATIC.negate());
    private static final Predicate<Field> ACCEPT_INSTANCE_FINAL = ACCEPT_ANY_FINAL.and(ACCEPT_ANY_STATIC.negate());
    private static final Predicate<Field> ACCEPT_STATIC_FINAL = ACCEPT_ANY_STATIC.and(ACCEPT_ANY_FINAL);
    private static final Predicate<Field> ACCEPT_STATIC_NON_FINAL = ACCEPT_ANY_STATIC.and(ACCEPT_ANY_FINAL.negate());

    @Override
    public boolean processInjection(Field field, Object fieldOwner, Set<Object> mockCandidates) {
        FieldInitializationReport report = initializeInjectMocksField(field, fieldOwner);

        // for each field in the class hierarchy
        boolean injectionOccurred = false;
        Class<?> fieldClass = report.fieldClass();
        Object fieldInstanceNeedingInjection = report.fieldInstance();
        InjectUnsafe injectUnsafe = parseInjectUnsafe(field);

        while (!fieldClass.equals(Object.class)) {
            injectionOccurred |= injectMockCandidates(fieldClass, fieldInstanceNeedingInjection, newMockSafeHashSet(mockCandidates), injectUnsafe);
            fieldClass = fieldClass.getSuperclass();
        }
        return injectionOccurred;
    }

    private FieldInitializationReport initializeInjectMocksField(Field field, Object fieldOwner) {
        try {
            return new FieldInitializer(fieldOwner, field).initialize();
        } catch (MockitoException e) {
            if(e.getCause() instanceof InvocationTargetException) {
                Throwable realCause = e.getCause().getCause();
                throw fieldInitialisationThrewException(field, realCause);
            }
            throw cannotInitializeForInjectMocksAnnotation(field.getName(),e.getMessage());
        }
    }


    private boolean injectMockCandidates(Class<?> awaitingInjectionClazz, Object injectee, Set<Object> mocks, InjectUnsafe injectUnsafe) {
        boolean injectionOccurred;
        List<Field> orderedCandidateInjecteeFields = orderedInstanceFieldsFrom(awaitingInjectionClazz, injectUnsafe);
        // pass 1
        injectionOccurred = injectMockCandidatesOnFields(mocks, injectee, orderedCandidateInjecteeFields);
        // pass 2
        injectionOccurred |= injectMockCandidatesOnFields(mocks, injectee, orderedCandidateInjecteeFields);
        return injectionOccurred;
    }

   private InjectUnsafe parseInjectUnsafe(Field injectMocksField) {
        InjectUnsafe injectUnsafe = injectMocksField.getAnnotation(InjectUnsafe.class);
        if (injectUnsafe == null) {
            injectUnsafe = new InjectUnsafeFallback();
        }

        return injectUnsafe;
    }

    private boolean injectMockCandidatesOnFields(Set<Object> mocks,
                                                 Object injectee,
                                                 List<Field> orderedCandidateInjecteeFields) {
        boolean injectionOccurred = false;
        for (Iterator<Field> it = orderedCandidateInjecteeFields.iterator(); it.hasNext(); ) {
            Field candidateField = it.next();
            Object injected = mockCandidateFilter.filterCandidate(mocks, candidateField, orderedCandidateInjecteeFields, injectee)
                                                 .thenInject();
            if (injected != null) {
                injectionOccurred = true;
                mocks.remove(injected);
                it.remove();
            }
        }
        return injectionOccurred;
    }

    List<Field> orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz, InjectUnsafe injectUnsafe) {
        ListUtil.Predicate<Field> fieldPredicate = predicateFromOverrides(injectUnsafe);

        List<Field> declaredFields = Arrays.asList(awaitingInjectionClazz.getDeclaredFields());
        declaredFields = ListUtil.filterBy(declaredFields, fieldPredicate);

        return sortSuperTypesLast(declaredFields);
    }

    private ListUtil.Predicate<Field> predicateFromOverrides(InjectUnsafe injectUnsafe) {
        Set<UnsafeFieldModifier> allow = EnumSet.copyOf(Arrays.asList(injectUnsafe.value()));

        Predicate<Field> filter = ACCEPT_INSTANCE_FIELD;

        if (allow.contains(UnsafeFieldModifier.STATIC_FINAL)) {
            filter = filter.or(ACCEPT_STATIC_FINAL);
        }

        if (allow.contains(UnsafeFieldModifier.FINAL)) {
            filter = filter.or(ACCEPT_INSTANCE_FINAL);
        }

        if (allow.contains(STATIC)) {
            filter = filter.or(ACCEPT_STATIC_NON_FINAL);
        }

        return filter;
    }
}
