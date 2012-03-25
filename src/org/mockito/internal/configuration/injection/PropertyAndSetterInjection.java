/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.configuration.injection;

import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.*;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;

/**
 * Inject mocks using first setters then fields, if no setters available.
 *
 * <p>
 * <u>Algorithm :<br></u>
 * for each field annotated by @InjectMocks
 *   <ul>
 *   <li>copy mocks set
 *   <li>initialize field annotated by @InjectMocks
 *   <li>for each field in @InjectMocks type ordered from sub-type to super-type
 *     <ul>
 *     <li>find mock candidate by type
 *     <li>if more than <b>*one*</b> candidate find mock candidate on name
 *     <li>if one mock candidate then
 *       <ul>
 *       <li>set mock by property setter if possible
 *       <li>else set mock by field injection
 *       </ul>
 *     <li>remove mock from mocks copy (mocks are just injected once)
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

    private final MockCandidateFilter mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
    private Comparator<Field> superTypesLast = new Comparator<Field>() {
        public int compare(Field field1, Field field2) {
            Class<?> field1Type = field1.getType();
            Class<?> field2Type = field2.getType();

            if(field1Type.isAssignableFrom(field2Type)) {
                return 1;
            }
            if(field2Type.isAssignableFrom(field1Type)) {
                return -1;
            }
            return 0;
        }
    };

    private ListUtil.Filter<Field> notFinalOrStatic = new ListUtil.Filter<Field>() {
        public boolean isOut(Field object) {
            return Modifier.isFinal(object.getModifiers()) || Modifier.isStatic(object.getModifiers());
        }
    };


    public boolean processInjection(Field field, Object fieldOwner, Set<Object> mockCandidates) {
        // Set<Object> mocksToBeInjected = new HashSet<Object>(mockCandidates);
        FieldInitializationReport report = null;
        try {
            report = new FieldInitializer(fieldOwner, field).initialize();
        } catch (MockitoException e) {
            if(e.getCause() instanceof InvocationTargetException) {
                Throwable realCause = e.getCause().getCause();
                new Reporter().fieldInitialisationThrewException(field, realCause);
            }
            new Reporter().cannotInitializeForInjectMocksAnnotation(field.getName(), e);
        }


        // for each field in the class hierarchy
        boolean injectionOccurred = false;
        Class<?> fieldClass = report.fieldClass();
        Object fieldInstanceNeedingInjection = report.fieldInstance();
        while (fieldClass != Object.class) {
            injectionOccurred |= injectMockCandidate(fieldClass, newMockSafeHashSet(mockCandidates), fieldInstanceNeedingInjection);
            fieldClass = fieldClass.getSuperclass();
        }
        return injectionOccurred;
    }



    private boolean injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object instance) {
        boolean injectionOccurred = false;
        for(Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
            Object injected = mockCandidateFilter.filterCandidate(mocks, field, instance).thenInject();
            if(injected != null) {
                injectionOccurred |= true;
                mocks.remove(injected);
            }
        }
        return injectionOccurred;
    }

    private List<Field> orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
        List<Field> declaredFields = Arrays.asList(awaitingInjectionClazz.getDeclaredFields());
        declaredFields = ListUtil.filter(declaredFields, notFinalOrStatic);

        Collections.sort(declaredFields, superTypesLast);

        return declaredFields;
    }

}
