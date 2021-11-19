/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.osgitest.testbundle;

import org.junit.Test;
import org.mockito.osgitest.otherbundle.Methods;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockClassInOtherBundleTest {

    @Test
    public void test() {
        Methods methods = mock(Methods.class);
        when(methods.intReturningMethod()).thenReturn(42);
        assertEquals(42, methods.intReturningMethod());
    }
}
