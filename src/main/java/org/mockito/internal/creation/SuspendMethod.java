package org.mockito.internal.creation;

import java.util.Arrays;

/**
 * Utilities for Kotlin Continuation-Passing-Style suspending function, detecting and trimming last hidden parameter.
 * See <a href="https://github.com/Kotlin/kotlin-coroutines/blob/master/kotlin-coroutines-informal.md#continuation-passing-style">Design docs for details</a>.
 */
public class SuspendMethod {
    private static final String KOTLIN_CONTINUATION = "kotlin.coroutines.experimental.Continuation";

    public static boolean isSuspend(Class<?>[] parameterTypes) {
        int n = parameterTypes.length;
        return n > 0 && parameterTypes[n - 1].getName().equals(KOTLIN_CONTINUATION);
    }

    public static Class<?>[] trimSuspendParameterTypes(Class<?>[] parameterTypes) {
        if (isSuspend(parameterTypes))
            return Arrays.copyOf(parameterTypes, parameterTypes.length - 1);
        return parameterTypes;
    }
}
