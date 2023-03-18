/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline;

import org.junit.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SubconstructorMockTest {

    @Test
    public void does_not_mock_subclass_constructor_for_superclass_mock() {
        try (MockedConstruction<SubClass> mocked = Mockito.mockConstruction(SubClass.class)) { }
        try (MockedConstruction<SuperClass> mocked = Mockito.mockConstruction(SuperClass.class)) {
            SubClass value = new SubClass();
            assertTrue(value.sup());
            assertTrue(value.sub());
        }
    }

    @Test
    public void does_mock_superclass_constructor_for_subclass_mock() {
        try (MockedConstruction<SuperClass> mocked = Mockito.mockConstruction(SuperClass.class)) { }
        try (MockedConstruction<SubClass> mocked = Mockito.mockConstruction(SubClass.class)) {
            SubClass value = new SubClass();
            assertFalse(value.sup());
            assertFalse(value.sub());
        }
    }

    public static class SuperClass {
        public boolean sup() {
            return true;
        }
    }

    public static class SubClass extends SuperClass {
        public boolean sub() {
            return true;
        }
    }
}
