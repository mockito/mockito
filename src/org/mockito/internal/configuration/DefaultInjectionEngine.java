/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Initializes mock/spies dependencies for fields annotated with &#064;InjectMocks
 * <p/>
 * See {@link org.mockito.MockitoAnnotations}
 */
public class DefaultInjectionEngine {

    private final MockCandidateFilter mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new FinalMockCandidateFilter()));
    private Comparator<Field> supertypesLast = new Comparator<Field>() {
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

    /**
     * Inject mocks in injectMocksFields, and initialize them if needed.
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
     *     <li>if more than *one* candidate find mock candidate on name
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
     * @param injectMocksFields Fields annotated by @InjectMocks
     * @param mocks Mocks
     * @param testClassInstance The test class instance
     */
    public void injectMocksOnFields(Set<Field> injectMocksFields, Set<Object> mocks, Object testClassInstance) {
        for (Field field : injectMocksFields) {
            Set<Object> mocksToBeInjected = new HashSet<Object>(mocks);
            Object injectMocksFieldInstance = null;
            try {
                injectMocksFieldInstance = new FieldInitializer(testClassInstance, field).initialize();
            } catch (MockitoException e) {
                new Reporter().cannotInitializeForInjectMocksAnnotation(field.getName(), e);
            }

            // for each field in the class hierarchy
            Class<?> fieldClass = injectMocksFieldInstance.getClass();
            while (fieldClass != Object.class) {
                injectMockCandidate(fieldClass, mocksToBeInjected, injectMocksFieldInstance);
                fieldClass = fieldClass.getSuperclass();
            }
        }
    }

    private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
        for(Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
            Object injected = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject();
            mocks.remove(injected);
        }
    }

    private Field[] orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
        Field[] declaredFields = awaitingInjectionClazz.getDeclaredFields();
        Arrays.sort(declaredFields, supertypesLast);
        return declaredFields;
    }

}
