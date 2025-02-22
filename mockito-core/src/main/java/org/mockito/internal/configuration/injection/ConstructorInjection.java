/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection;

import static org.mockito.internal.exceptions.Reporter.fieldInitialisationThrewException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

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
    public boolean processInjection(Field field, Object fieldOwner, Map<Field, Object> mockCandidates) {
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

        final Map<Field, Object> objects;
        final ConstructorArgumentVoter voter;

        public SimpleArgumentResolver(Map<Field, Object> objects) {
            this.objects = objects;
            this.voter = new ConstructorArgumentVoter();
        }

        @Override
        public Object[] resolveTypeInstances(Constructor<?> constructor) {
            Parameter[] parameters = constructor.getParameters();
            int n = constructor.getParameterCount();
            Object[] args = new Object[n];
            for (int i = 0; i < n; i++) {
                args[i] = voter.vote(objects, parameters[i]);
            }
            return args;
        }

    }

    /**
     * vote by generic type, type, and field name.
     *
     * @author 陈强
     */
    static class ConstructorArgumentVoter {

        public Object vote(Map<Field, Object> mocks, Parameter param) {
            Map<Field, Integer> candidatesVote = new HashMap<>();
            for (Map.Entry<Field, Object> entry : mocks.entrySet()) {
                Field field = entry.getKey();
                int score = deepTypeMatch(field.getGenericType(), param.getParameterizedType());
                if (score > 0) {
                    if (field.getName().equals(param.getName())) {
                        score++;
                    }
                    candidatesVote.put(field, score);
                }
            }
            if (!candidatesVote.isEmpty()) {
                return mocks.get(mostMatch(candidatesVote));
            }
            return null;
        }

        protected int deepTypeMatch(Type mockType, Type targetType) {
            if (mockType instanceof Class && targetType instanceof Class) {
                Class<?> c1 = (Class<?>) mockType;
                Class<?> c2 = (Class<?>) targetType;
                return c1.isAssignableFrom(c2) ? 3 : 0;
            }
            if (mockType instanceof TypeVariable && targetType instanceof TypeVariable) {
                return 3;
            }
            if (mockType instanceof TypeVariable || targetType instanceof TypeVariable) {
                return 2;
            }
            int vote = 0;
            if (mockType instanceof ParameterizedType && targetType instanceof ParameterizedType) {
                ParameterizedType pt1 = (ParameterizedType) mockType;
                ParameterizedType pt2 = (ParameterizedType) targetType;
                vote += deepTypeMatch(pt1.getRawType(), pt2.getRawType());
                if (vote > 0) {
                    Type[] t1 = pt1.getActualTypeArguments();
                    Type[] t2 = pt2.getActualTypeArguments();
                    if (t1.length == t2.length) {
                        for (int i = 0; i < t1.length; i++) {
                            vote += deepTypeMatch(t1[i], t2[i]);
                        }
                    }
                }
            } else if (mockType instanceof ParameterizedType) {
                vote += deepTypeMatch(((ParameterizedType) mockType).getRawType(), targetType);
            } else {
                vote += deepTypeMatch(mockType, ((ParameterizedType) targetType).getRawType());
            }
            return vote;
        }

        protected Field mostMatch(Map<Field, Integer> candidatesVote) {
            NavigableMap<Integer, Field> tree = new TreeMap<>();
            for (Map.Entry<Field, Integer> entry : candidatesVote.entrySet()) {
                tree.put(entry.getValue(), entry.getKey());
            }
            return tree.lastEntry().getValue();
        }

    }

}
