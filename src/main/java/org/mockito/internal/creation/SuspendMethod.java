/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation;

import java.util.Arrays;

/**
 * Utilities for Kotlin Continuation-Passing-Style suspending function, detecting and trimming last hidden parameter.
 * See <a href="https://github.com/Kotlin/kotlin-coroutines/blob/master/kotlin-coroutines-informal.md#continuation-passing-style">Design docs for details</a>.
 */
public class SuspendMethod {
    private static final String KOTLIN_CONTINUATION = "kotlin.coroutines.experimental.Continuation";

    public static Class<?>[] trimSuspendParameterTypes(Class<?>[] parameterTypes) {
        int n = parameterTypes.length;
        if (n > 0 && parameterTypes[n - 1].getName().equals(KOTLIN_CONTINUATION))
            return Arrays.copyOf(parameterTypes, n - 1);
        return parameterTypes;
    }
}
