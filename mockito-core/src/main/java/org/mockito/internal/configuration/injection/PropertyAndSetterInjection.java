/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection;

import static org.mockito.internal.exceptions.Reporter.cannotInitializeForInjectMocksAnnotation;
import static org.mockito.internal.exceptions.Reporter.fieldInitialisationThrewException;
import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;
import static org.mockito.internal.util.reflection.SuperTypesLastSorter.sortSuperTypesLast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TerminalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;

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
                    new NameBasedCandidateFilter(new TerminalMockCandidateFilter()));

    @Override
    public boolean processInjection(
            Field injectMocksField, Object injectMocksFieldOwner, Set<Object> mockCandidates) {
        FieldInitializationReport report =
                initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);

        // for each field in the class hierarchy
        boolean injectionOccurred = false;
        Class<?> fieldClass = report.fieldClass();
        Object fieldInstanceNeedingInjection = report.fieldInstance();
        while (fieldClass != Object.class) {
            injectionOccurred |=
                    injectMockCandidates(
                            fieldClass,
                            fieldInstanceNeedingInjection,
                            injectMocksField,
                            newMockSafeHashSet(mockCandidates));
            fieldClass = fieldClass.getSuperclass();
        }
        return injectionOccurred;
    }

    private FieldInitializationReport initializeInjectMocksField(Field field, Object fieldOwner) {
        try {
            return new FieldInitializer(fieldOwner, field).initialize();
        } catch (MockitoException e) {
            if (e.getCause() instanceof InvocationTargetException) {
                Throwable realCause = e.getCause().getCause();
                throw fieldInitialisationThrewException(field, realCause);
            }
            throw cannotInitializeForInjectMocksAnnotation(field.getName(), e.getMessage());
        }
    }

    private boolean injectMockCandidates(
            Class<?> awaitingInjectionClazz,
            Object injectee,
            Field injectMocksField,
            Set<Object> mocks) {
        boolean injectionOccurred;
        List<Field> orderedCandidateInjecteeFields =
                orderedInstanceFieldsFrom(awaitingInjectionClazz);
        // pass 1
        injectionOccurred =
                injectMockCandidatesOnFields(
                        mocks, injectee, injectMocksField, false, orderedCandidateInjecteeFields);
        // pass 2
        injectionOccurred |=
                injectMockCandidatesOnFields(
                        mocks,
                        injectee,
                        injectMocksField,
                        injectionOccurred,
                        orderedCandidateInjecteeFields);
        return injectionOccurred;
    }

    private boolean injectMockCandidatesOnFields(
            Set<Object> mocks,
            Object injectee,
            Field injectMocksField,
            boolean injectionOccurred,
            List<Field> orderedCandidateInjecteeFields) {
        for (Iterator<Field> it = orderedCandidateInjecteeFields.iterator(); it.hasNext(); ) {
            Field candidateField = it.next();
            Object injected =
                    mockCandidateFilter
                            .filterCandidate(
                                    mocks,
                                    candidateField,
                                    orderedCandidateInjecteeFields,
                                    injectee,
                                    injectMocksField)
                            .thenInject();
            if (injected != null) {
                injectionOccurred |= true;
                mocks.remove(injected);
                it.remove();
            }
        }
        return injectionOccurred;
    }

    private List<Field> orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
        return sortSuperTypesLast(
                Arrays.stream(awaitingInjectionClazz.getDeclaredFields())
                        .filter(
                                field ->
                                        !Modifier.isFinal(field.getModifiers())
                                                && !Modifier.isStatic(field.getModifiers()))
                        .collect(Collectors.toList()));
    }
}
