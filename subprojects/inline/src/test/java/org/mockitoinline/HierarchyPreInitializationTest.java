/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline;

import org.junit.Test;

import static org.mockito.Mockito.mock;

public class HierarchyPreInitializationTest {

    @Test
    @SuppressWarnings("CheckReturnValue")
    public void testOrder() {
        mock(MyClass.class);
        mock(TestSubInterface.class);
    }

    public interface TestInterface {

        @SuppressWarnings("unused")
        MyClass INSTANCE = new MyClass().probe();
    }

    public interface TestSubInterface extends TestInterface {
    }

    public static class MyClass {

        private final Object obj;

        public MyClass() {
            obj = new Object();
        }

        public MyClass probe() {
            if (obj == null) {
                throw new RuntimeException();
            }
            return this;
        }
    }
}
