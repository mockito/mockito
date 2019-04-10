package org.mockito.memorytest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class ShouldNotStarveMemoryOnLargeStackTraceInvocationsTest {

    private static final int STACK_TRACE_DEPTH = 1000;
    private static final int INVOCATIONS_ON_STACK_TRACE_LEVEL = 100;

    @Test
    public void large_stack_trace_invocations_should_not_starve_memory() {
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
