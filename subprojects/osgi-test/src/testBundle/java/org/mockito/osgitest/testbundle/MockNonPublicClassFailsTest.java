/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.osgitest.testbundle;

import org.mockito.exceptions.base.MockitoException;

import static org.mockito.Mockito.mock;

public class MockNonPublicClassFailsTest implements Runnable {

    static class NonPublicClass {}

    @Override
    public void run() {
        try {
            NonPublicClass nonPublicClass = mock(NonPublicClass.class);
            throw new AssertionError();
        } catch(MockitoException e) {
            // Expected: The type is not public and its mock class is loaded by a different class loader.
        }
    }
}
