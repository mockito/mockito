/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

public class StressTest {
    public class TestClass {
        public String getStuff() {
            return "A";
        }
    }

    @Test
    public void call_a_lot_of_mocks() {
        //This requires smaller heap set for the test process, see "inline.gradle"
        for (int i = 0; i < 40000; i++) {
            TestClass mock = mock(TestClass.class);
            when(mock.getStuff()).thenReturn("B");
            assertEquals("B", mock.getStuff());

            TestClass serializableMock = mock(TestClass.class, withSettings().serializable());
            when(serializableMock.getStuff()).thenReturn("C");
            assertEquals("C", serializableMock.getStuff());

            if (i % 1024 == 0) {
                System.out.println(i + "/40000 mocks called");
            }
        }
    }
}
