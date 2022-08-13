/*
 * Copyright (c) 2022 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.memorytest;

import com.sun.management.ThreadMXBean;
import org.junit.Test;
import org.mockito.internal.debugging.LocationFactory;

import java.lang.management.ManagementFactory;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class LocationFactoryAllocationRateTest {
    private static final int REPEAT = 1000;
    private static final int RECURSION_LIMIT = 1000;
    private static final double EXPECTED_IMPROVEMENT = expectedImprovement();

    private static final ThreadMXBean memoryBean =
        (ThreadMXBean) ManagementFactory.getThreadMXBean();


    @Test
    public void shouldAllocateMuchLessMemoryThanThrowable() {
        // On Java 8, this will use the internal approach. On Java 9, the StackWalker approach will
        // be used.
        new Throwable().fillInStackTrace();
        LocationFactory.create();
        long baseline =
            countMemoryAllocations(
                () ->
                    recurseAndThen(
                        RECURSION_LIMIT,
                        repeat(() -> new Throwable().fillInStackTrace())));
        long actual =
            countMemoryAllocations(
                () ->
                    recurseAndThen(
                        RECURSION_LIMIT,
                        repeat(() -> LocationFactory.create(false))));
        assertThat(actual * EXPECTED_IMPROVEMENT)
            .as(
                "stack walker approach (%d) expected to be at least %fx better than exception approach (%d)",
                actual, EXPECTED_IMPROVEMENT, baseline)
            .isLessThan(baseline);
    }

    private static long countMemoryAllocations(Runnable someTask) {
        long threadId = Thread.currentThread().getId();
        long atStart = memoryBean.getThreadAllocatedBytes(threadId);
        someTask.run();
        return memoryBean.getThreadAllocatedBytes(threadId) - atStart;
    }

    private static void recurseAndThen(int count, Runnable runnable) {
        if (count <= 0) {
            runnable.run();
        } else {
            recurseAndThen(count - 1, runnable);
        }
    }

    private static Runnable repeat(Runnable task) {
        return () -> IntStream.range(0, REPEAT).forEach(index -> task.run());
    }

    private static double expectedImprovement() {
        try {
            Class.forName("java.lang.StackWalker");
            return 20;
        } catch (ClassNotFoundException e) {
            return 1.5;
        }
    }
}
