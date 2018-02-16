/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StressTest {
    public class TestClass {
        public String returnA() {
            return "A";
        }
    }

    @Test
    public void call_a_lot_of_mocks() {
        for (int i = 0; i < 40000; i++) {
            TestClass t = mock(TestClass.class);
            when(t.returnA()).thenReturn("B");
            assertEquals("B", t.returnA());

            if (i % 1024 == 0) {
                System.out.println(i + "/40000 mocks called");
            }
        }
    }
}
