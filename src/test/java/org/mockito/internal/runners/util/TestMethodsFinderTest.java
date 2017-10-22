/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.runners.util;

import org.junit.Test;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestMethodsFinderTest extends TestBase {

    public static class HasTests {
        @Test public void someTest() {}
    }

    static class DoesNotHaveTests {
        public void someTest() {}
    }

    @Test
    public void shouldKnowWhenClassHasTests() {
        assertTrue(TestMethodsFinder.hasTestMethods(HasTests.class));
        assertFalse(TestMethodsFinder.hasTestMethods(DoesNotHaveTests.class));
    }
}
