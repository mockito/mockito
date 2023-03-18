/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline.bugs;

import org.junit.Test;

import static org.mockito.Mockito.framework;
import static org.mockito.Mockito.spy;

public class SelfSpyReferenceMemoryLeakTest {
    private static final int ARRAY_LENGTH = 1 << 20;  // 4 MB

    @Test
    public void no_memory_leak_when_spy_holds_reference_to_self() {
        for (int i = 0; i < 100; ++i) {
            final DeepRefSelfClass instance = spy(new DeepRefSelfClass());
            instance.refInstance(instance);

            framework().clearInlineMocks();
        }
    }

    private static class DeepRefSelfClass {
        private final DeepRefSelfClass[] array = new DeepRefSelfClass[1];

        private final int[] largeArray = new int[ARRAY_LENGTH];

        private void refInstance(DeepRefSelfClass instance) {
            array[0] = instance;
        }
    }
}
