/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.configuration.injection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.mockito.InjectUnsafe;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TerminalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;

import static org.mockito.internal.exceptions.Reporter.cannotInitializeForInjectMocksAnnotation;
import static org.mockito.internal.exceptions.Reporter.fieldInitialisationThrewException;
import static org.mockito.internal.util.collections.ListUtil.combineOr;
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
 */
public class PropertyAndSetterInjection extends MockInjectionStrategy {

    private final MockCandidateFilter mockCandidateFilter =
            new TypeBasedCandidateFilter(
                    new NameBasedCandidateFilter(
                            new TerminalMockCandidateFilter()));


    private final ListUtil.Filter<Field> removeNothing = new ListUtil.Filter<Field>() {
        @Override
        public String toString() {
            return "removeNothing";
        }

        public boolean isOut(Field object) {
            return false;
        }
    };

    private final ListUtil.Filter<Field> removeStatic = new ListUtil.Filter<Field>() {
        @Override
        public String toString() {
            return "removeStatic";
        }

        public boolean isOut(Field object) {
            return Modifier.isStatic(object.getModifiers());
        }
    };

    private final ListUtil.Filter<Field> removeStaticFinal = new ListUtil.Filter<Field>() {
        @Override
        public String toString() {
            return "removeStaticFinal";
        }

        public boolean isOut(Field object) {
            return Modifier.isStatic(object.getModifiers()) && Modifier.isFinal(object.getModifiers());
        }
    };

    private final ListUtil.Filter<Field> removeInstanceFinalFields = new ListUtil.Filter<Field>() {
        @Override
        public String toString() {
            return "removeInstanceFinalFields";
        }
        public boolean isOut(Field object) {
            return !Modifier.isStatic(object.getModifiers()) && Modifier.isFinal(object.getModifiers());
        }
    };


    public boolean processInjection(Field injectMocksField, Object injectMocksFieldOwner, Set<Object> mockCandidates) {
        FieldInitializationReport report = initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);

        // for each field in the class hierarchy
        boolean injectionOccurred = false;
        Class<?> fieldClass = report.fieldClass();
        Object fieldInstanceNeedingInjection = report.fieldInstance();
        InjectUnsafe injectUnsafe = parseInjectUnsafe(injectMocksField);

        while (fieldClass != Object.class) {
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
            injectUnsafe = new InjectUnsafeDefaults();
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

    private List<Field> orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz, InjectUnsafe injectUnsafe) {
        ListUtil.Filter<Field> fieldFilters = filtersFromOverrides(injectUnsafe);

        List<Field> declaredFields = Arrays.asList(awaitingInjectionClazz.getDeclaredFields());
        declaredFields = ListUtil.filter(declaredFields, fieldFilters);

        return sortSuperTypesLast(declaredFields);
    }

    private ListUtil.Filter<Field> filtersFromOverrides(InjectUnsafe injectUnsafe) {
        ListUtil.Filter<Field> staticFieldFilter;

        switch (injectUnsafe.staticFields()) {
            case STATIC_FINAL:
                staticFieldFilter = this.removeNothing;
                break;
            case STATIC:
                staticFieldFilter = this.removeStaticFinal;
                break;
            default:
                staticFieldFilter = this.removeStatic;
        }

        ListUtil.Filter<Field> instanceFieldFilter;
        switch (injectUnsafe.instanceFields()) {
            case FINAL:
                instanceFieldFilter = this.removeNothing;
                break;
            default:
                instanceFieldFilter = this.removeInstanceFinalFields;
        }

        return combineOr(staticFieldFilter, instanceFieldFilter);
    }
}
