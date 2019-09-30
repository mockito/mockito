/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline.bugs;

import org.junit.Test;

import static org.mockito.Mockito.framework;
import static org.mockito.Mockito.mock;

public class CyclicMockMethodArgumentMemoryLeakTest {
    private static final int ARRAY_LENGTH = 1 << 20;  // 4 MB

    @Test
    public void no_memory_leak_when_cyclically_calling_method_with_mocks() {
        for (int i = 0; i < 100; ++i) {
            final A a = mock(A.class);
            a.largeArray = new int[ARRAY_LENGTH];
            final B b = mock(B.class);

            a.accept(b);
            b.accept(a);

            framework().clearInlineMocks();
        }
    }

    private static class A {
        private int[] largeArray;

        void accept(B b) {}
    }

    private static class B {
        void accept(A a) {}
    }
}
