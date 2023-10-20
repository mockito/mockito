/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection;

import static org.mockito.internal.exceptions.Reporter.fieldInitialisationThrewException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.FieldInitializer.ConstructorArgumentResolver;

/**
 * Injection strategy based on constructor.
 *
 * <p>
 * The strategy will search for the constructor with most parameters
 * and try to resolve mocks by type.
 * </p>
 *
 * <blockquote>
 * TODO on missing mock type, shall it abandon or create "noname" mocks.
 * TODO and what if the arg type is not mockable.
 * </blockquote>
 *
 * <p>
 * For now the algorithm tries to create anonymous mocks if an argument type is missing.
 * If not possible the algorithm abandon resolution.
 * </p>
 */
public class ConstructorInjection extends MockInjectionStrategy {

    public ConstructorInjection() {}

    @Override
    public boolean processInjection(Field field, Object fieldOwner, Set<Object> mockCandidates) {
        try {
            SimpleArgumentResolver simpleArgumentResolver =
                new SimpleArgumentResolver(mockCandidates);
            FieldInitializationReport report =
                new FieldInitializer(fieldOwner, field, simpleArgumentResolver).initialize();

            return report.fieldWasInitializedUsingContructorArgs();
        } catch (MockitoException e) {
            if (e.getCause() instanceof InvocationTargetException) {
                Throwable realCause = e.getCause().getCause();
                throw fieldInitialisationThrewException(field, realCause);
            }
            // other causes should be fine
            return false;
        }
    }

    /**
     * Returns mocks that match the argument type, if not possible assigns null.
     */
    static class SimpleArgumentResolver implements ConstructorArgumentResolver {
        final Set<Object> objects;

        public SimpleArgumentResolver(Set<Object> objects) {
            this.objects = objects;
        }
        /**
         * types -> actual constructor types
         * argTypes -> fake constructor types
         * hashMap -> mocks
         */
        @Override
        public Object[] resolveTypeInstancesGeneric(HashMap<String, Type> hashMap, Type[] types) {
            // list of mocks not constructors
            List<Object> argumentInstances = new ArrayList<>(types.length);
            for (Type parameterType: types) {
                argumentInstances.add(objectThatIsAssignableFromGeneric(hashMap,parameterType));
            }
            return argumentInstances.toArray();

        }

        @Override
        public Object[] resolveTypeInstances(Class<?>... argTypes) {
            List<Object> argumentInstances = new ArrayList<>(argTypes.length);
            for (Class<?> argType : argTypes) {
                argumentInstances.add(objectThatIsAssignableFrom(argType));
            }
            return argumentInstances.toArray();
        }

        private Object objectThatIsAssignableFrom(Class<?> argType) {
            for (Object object : objects) {
                if (argType.isAssignableFrom(object.getClass())) {
                    return object;
                }
            }
            return null;
        }

        private Object objectThatIsAssignableFromGeneric(HashMap<String, Type> hashMap, Type parameterType) {
            Class<?> typeclass = null;
            // converts parameter Type into Class
            if (parameterType instanceof Class) {
                typeclass = (Class<?>) parameterType;
            } else if (parameterType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) parameterType;
                typeclass = (Class<?>) parameterizedType.getRawType();
            }
            assert typeclass != null;
            // for each mock object
            for (Object object : objects) {
                if (typeclass.isAssignableFrom(object.getClass())) {
                    // if mock object class(including parameterized classes) matches class of constructor class
                    if (hashMap.get(object.toString()) == null) continue;
                    if (hashMap.get(object.toString()).equals(parameterType)) return object;
                }
            }
            return null;
        }
    }
}
