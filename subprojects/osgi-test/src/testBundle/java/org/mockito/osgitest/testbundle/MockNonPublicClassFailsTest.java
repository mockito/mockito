/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.osgitest.testbundle;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;

import static org.mockito.Mockito.mock;

public class MockNonPublicClassFailsTest {

    static class NonPublicClass {}

    @Test(expected = MockitoException.class)
    public void test() {
            // Expected: The type is not public and its mock class is loaded by a different class loader.
            NonPublicClass nonPublicClass = mock(NonPublicClass.class);
    }
}
