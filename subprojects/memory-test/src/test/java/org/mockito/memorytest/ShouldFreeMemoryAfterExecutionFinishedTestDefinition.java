/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.memorytest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ShouldFreeMemoryAfterExecutionFinishedTestDefinition {
    private static final int ELEMENT_COUNT = 4_000_000;

    private MemoryHog memoryHog;

    @Before
    public void setUp() {
        memoryHog = new MemoryHog();
    }

    @Test
    public void test() {
        memoryHog.run();
    }

    static class MemoryHog {
        private Long[] values = new Long[ELEMENT_COUNT];

        void run() {
            for (int i = 0; i < ELEMENT_COUNT; i++) {
                values[i] = (long) i;
            }
        }
    }
}
