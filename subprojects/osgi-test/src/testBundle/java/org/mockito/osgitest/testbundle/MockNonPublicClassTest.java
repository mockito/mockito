/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.osgitest.testbundle;

import org.junit.Test;

import static org.mockito.Mockito.mock;

public class MockNonPublicClassTest {

    static class NonPublicClass {}

    @Test
    public void test_non_public_class() {
        NonPublicClass nonPublicClass = mock(NonPublicClass.class);
    }
}
