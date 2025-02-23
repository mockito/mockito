/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.junitrule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoTestRule;

public class MockitoJUnitTestRuleTest {

    @Rule public MockitoTestRule mockitoRule = MockitoJUnit.testRule(this);

    // Fixes #1578: Protect against multiple execution.
    @Rule public MockitoTestRule mockitoRule2 = mockitoRule;

    @Mock private Injected injected;

    @InjectMocks private InjectInto injectInto;

    @Test
    public void testInjectMocks() throws Exception {
        assertNotNull("Mock created", injected);
        assertNotNull("Object created", injectInto);
        assertEquals("A injected into B", injected, injectInto.getInjected());
    }

    public static class Injected {}

    public static class InjectInto {
        private Injected injected;

        public Injected getInjected() {
            return injected;
        }
    }
}
