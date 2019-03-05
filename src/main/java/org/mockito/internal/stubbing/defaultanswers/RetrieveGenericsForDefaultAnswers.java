/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockCreationSettings;

class RetrieveGenericsForDefaultAnswers {

    private static final MockitoCore MOCKITO_CORE = new MockitoCore();

    static Object returnTypeForMockWithCorrectGenerics(
        InvocationOnMock invocation, AnswerCallback answerCallback) {
        Class<?> type = invocation.getMethod().getReturnType();

        final Type returnType = invocation.getMethod().getGenericReturnType();

        Object defaultReturnValue = null;

        if (returnType instanceof TypeVariable) {
            type = findTypeFromGeneric(invocation, (TypeVariable) returnType);
            if (type != null) {
                defaultReturnValue = delegateChains(type);
            }
        }

        if (defaultReturnValue != null) {
            return defaultReturnValue;
        }

        if (type != null) {
            if (!MOCKITO_CORE.isTypeMockable(type)) {
                return null;
            }

            return answerCallback.apply(type);
        }

        return answerCallback.apply(null);
    }

    /**
     * Try to resolve the result value using {@link ReturnsEmptyValues} and {@link ReturnsMoreEmptyValues}.
     *
     * This will try to use all parent class (superclass & interfaces) to retrieve the value..
     *
     * @param type the return type of the method
     * @return a non-null instance if the type has been resolve. Null otherwise.
     */
    private static Object delegateChains(final Class<?> type) {
        final ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object result = returnsEmptyValues.returnValueFor(type);

        if (result == null) {
            Class<?> emptyValueForClass = type;
            while (emptyValueForClass != null && result == null) {
                final Class<?>[] classes = emptyValueForClass.getInterfaces();
                for (Class<?> clazz : classes) {
                    result = returnsEmptyValues.returnValueFor(clazz);
                    if (result != null) {
                        break;
                    }
                }
                emptyValueForClass = emptyValueForClass.getSuperclass();
            }
        }

        if (result == null) {
            result = new ReturnsMoreEmptyValues().returnValueFor(type);
        }

        return result;
    }

    /**
     * Retrieve the expected type when it came from a primitive. If the type cannot be retrieve, return null.
     *
     * @param invocation the current invocation
     * @param returnType the expected return type
     * @return the type or null if not found
     */
    private static Class<?> findTypeFromGeneric(final InvocationOnMock invocation, final TypeVariable returnType) {
        // Class level
        final MockCreationSettings mockSettings = MockUtil.getMockHandler(invocation.getMock()).getMockSettings();
        final GenericMetadataSupport returnTypeSupport = GenericMetadataSupport
            .inferFrom(mockSettings.getTypeToMock())
            .resolveGenericReturnType(invocation.getMethod());
        final Class<?> rawType = returnTypeSupport.rawType();

        // Method level
        if (rawType == Object.class) {
            return findTypeFromGenericInArguments(invocation, returnType);
        }
        return rawType;
    }

    /**
     * Find a return type using generic arguments provided by the calling method.
     *
     * @param invocation the current invocation
     * @param returnType the expected return type
     * @return the return type or null if the return type cannot be found
     */
    private static Class<?> findTypeFromGenericInArguments(final InvocationOnMock invocation, final TypeVariable returnType) {
        final Type[] parameterTypes = invocation.getMethod().getGenericParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Type argType = parameterTypes[i];
            if (returnType.equals(argType)) {
                Object argument = invocation.getArgument(i);

                if (argument == null) {
                    return null;
                }

                return argument.getClass();
            }
            if (argType instanceof GenericArrayType) {
                argType = ((GenericArrayType) argType).getGenericComponentType();
                if (returnType.equals(argType)) {
                    return invocation.getArgument(i).getClass();
                }
            }
        }
        return null;
    }

    interface AnswerCallback {
        Object apply(Class<?> type);
    }
}
