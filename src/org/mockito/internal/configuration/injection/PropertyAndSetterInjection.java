/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.configuration.injection;

import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TerminalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

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
 */
public class PropertyAndSetterInjection extends MockInjectionStrategy {

    private final MockCandidateFilter mockCandidateFilter =
            new TypeBasedCandidateFilter(
                    new NameBasedCandidateFilter(
                            new TerminalMockCandidateFilter()));

    private final ListUtil.Filter<Field> notFinalOrStatic = new ListUtil.Filter<Field>() {
        public boolean isOut(final Field object) {
            return Modifier.isFinal(object.getModifiers()) || Modifier.isStatic(object.getModifiers());
        }
    };


    public boolean processInjection(final Field injectMocksField, final Object injectMocksFieldOwner, final Set<Object> mockCandidates) {
        final FieldInitializationReport report = initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);

        // for each field in the class hierarchy
        boolean injectionOccurred = false;
        Class<?> fieldClass = report.fieldClass();
        final Object fieldInstanceNeedingInjection = report.fieldInstance();
        while (fieldClass != Object.class) {
            injectionOccurred |= injectMockCandidates(fieldClass, fieldInstanceNeedingInjection, newMockSafeHashSet(mockCandidates));
            fieldClass = fieldClass.getSuperclass();
        }
        return injectionOccurred;
    }

    private FieldInitializationReport initializeInjectMocksField(final Field field, final Object fieldOwner) {
        try {
            return new FieldInitializer(fieldOwner, field).initialize();
        } catch (final MockitoException e) {
            if(e.getCause() instanceof InvocationTargetException) {
                final Throwable realCause = e.getCause().getCause();
                new Reporter().fieldInitialisationThrewException(field, realCause);
            }
            new Reporter().cannotInitializeForInjectMocksAnnotation(field.getName(), e);
            throw new IllegalStateException("never thrown");
        }
    }


    private boolean injectMockCandidates(final Class<?> awaitingInjectionClazz, final Object injectee, final Set<Object> mocks) {
        boolean injectionOccurred;
        final List<Field> orderedCandidateInjecteeFields = orderedInstanceFieldsFrom(awaitingInjectionClazz);
        // pass 1
        injectionOccurred = injectMockCandidatesOnFields(mocks, injectee, false, orderedCandidateInjecteeFields);
        // pass 2
        injectionOccurred |= injectMockCandidatesOnFields(mocks, injectee, injectionOccurred, orderedCandidateInjecteeFields);
        return injectionOccurred;
    }

    private boolean injectMockCandidatesOnFields(final Set<Object> mocks,
                                                 final Object injectee,
                                                 boolean injectionOccurred,
                                                 final List<Field> orderedCandidateInjecteeFields) {
        for (final Iterator<Field> it = orderedCandidateInjecteeFields.iterator(); it.hasNext();) {
            final Field candidateField = it.next();
            final Object injected = mockCandidateFilter.filterCandidate(mocks, candidateField, orderedCandidateInjecteeFields, injectee)
                                                 .thenInject();
            if (injected != null) {
                injectionOccurred |= true;
                mocks.remove(injected);
                it.remove();
            }
        }
        return injectionOccurred;
    }

    private List<Field> orderedInstanceFieldsFrom(final Class<?> awaitingInjectionClazz) {
        List<Field> declaredFields = Arrays.asList(awaitingInjectionClazz.getDeclaredFields());
        declaredFields = ListUtil.filter(declaredFields, notFinalOrStatic);

        return new SuperTypesLastSorter().sort(declaredFields);
    }
}
