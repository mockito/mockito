package org.mockito.internal.runners.util;

import org.junit.Test;
import org.mockitoutil.TestBase;

public class TestMethodsFinderTest extends TestBase {

    static class HasTests {
        @Test public void someTest() {}
    }

    static class DoesNotHaveTests {
        public void someTest() {}
    }

    @Test
    public void shouldKnowWhenClassHasTests() {
        assertTrue(new TestMethodsFinder().hasTestMethods(HasTests.class));
        assertFalse(new TestMethodsFinder().hasTestMethods(DoesNotHaveTests.class));
    }
}