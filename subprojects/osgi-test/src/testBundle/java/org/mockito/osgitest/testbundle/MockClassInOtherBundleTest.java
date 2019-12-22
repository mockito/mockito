/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.osgitest.testbundle;

import org.mockito.osgitest.otherbundle.Methods;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockClassInOtherBundleTest implements Runnable {

    @Override
    public void run() {
        Methods methods = mock(Methods.class);
        when(methods.intReturningMethod()).thenReturn(42);
        if (methods.intReturningMethod() != 42) {
            throw new AssertionError();
        }
    }
}
