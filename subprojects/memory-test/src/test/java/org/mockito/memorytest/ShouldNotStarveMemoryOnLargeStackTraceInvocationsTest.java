/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.memorytest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assume;
import org.junit.Test;

public class ShouldNotStarveMemoryOnLargeStackTraceInvocationsTest {

    private static final int STACK_TRACE_DEPTH = 1000;
    private static final int INVOCATIONS_ON_STACK_TRACE_LEVEL = 100;

    private static boolean supported = false;

    static {
        try {
            Class.forName("sun.misc.SharedSecrets")
                .getMethod("getJavaLangAccess")
                .invoke(null);
            Class.forName("sun.misc.JavaLangAccess")
                .getMethod("getStackTraceElement", Throwable.class, int.class);

            supported = true;
        } catch (Exception ignored) {
        }
    }

    @Test
    public void large_stack_trace_invocations_should_not_starve_memory() {
        Assume.assumeTrue(supported);
        Dummy mock = mock(Dummy.class);

        when(mock.calculate(anyInt())).thenReturn(42);

        assertThat(performComputationForDepth(mock, STACK_TRACE_DEPTH)).isEqualTo(42);
    }

    private int performComputationForDepth(Dummy mock, int i) {
        if (i > 0) {
            for (int j = 0; j < INVOCATIONS_ON_STACK_TRACE_LEVEL; j++) {
                mock.calculate(j);
            }

            return mock.calculate(performComputationForDepth(mock, i - 1));
        }

        return 1;
    }

    interface Dummy {
        int calculate(int fib);
    }
}
