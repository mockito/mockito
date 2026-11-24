/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;

import org.mockito.internal.util.Primitives;

/**
 * Utilities for Kotlin Continuation-Passing-Style suspending function, detecting and trimming last hidden parameter.
 * See <a href="https://github.com/Kotlin/kotlin-coroutines/blob/master/kotlin-coroutines-informal.md#continuation-passing-style">Design docs for details</a>.
 */
public final class SuspendMethod {
    private static final String KOTLIN_EXPERIMENTAL_CONTINUATION =
            "kotlin.coroutines.experimental.Continuation";
    private static final String KOTLIN_CONTINUATION = "kotlin.coroutines.Continuation";

    public static Class<?>[] trimSuspendParameterTypes(Class<?>[] parameterTypes) {
        int n = parameterTypes.length;
        if (n > 0 && isContinuationType(parameterTypes[n - 1])) {
            return Arrays.copyOf(parameterTypes, n - 1);
        }
        return parameterTypes;
    }

    /**
     * A suspend function returning a primitive-like Kotlin type (e.g. {@code Boolean}) compiles to a
     * method returning {@code Object}, with the real return type only visible as the type argument of
     * the trailing {@code Continuation<? super T>} parameter. Callers that use the result unbox it, so
     * returning {@code null} causes a {@link NullPointerException}.
     *
     * @return the default value for the primitive wrapper the suspend function returns, or {@code null}
     *     if the method is not a suspend function or does not return a primitive wrapper
     */
    public static Object defaultPrimitiveReturnValue(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        int n = parameterTypes.length;
        if (n == 0 || !isContinuationType(parameterTypes[n - 1])) {
            return null;
        }
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        if (genericParameterTypes.length != n
                || !(genericParameterTypes[n - 1] instanceof ParameterizedType)) {
            return null;
        }
        Type resultType =
                ((ParameterizedType) genericParameterTypes[n - 1]).getActualTypeArguments()[0];
        if (resultType instanceof WildcardType) {
            Type[] lowerBounds = ((WildcardType) resultType).getLowerBounds();
            resultType = lowerBounds.length == 1 ? lowerBounds[0] : null;
        }
        return resultType instanceof Class ? Primitives.defaultValue((Class<?>) resultType) : null;
    }

    private static boolean isContinuationType(Class<?> parameterType) {
        String name = parameterType.getName();
        return name.equals(KOTLIN_CONTINUATION) || name.equals(KOTLIN_EXPERIMENTAL_CONTINUATION);
    }

    private SuspendMethod() {}
}
