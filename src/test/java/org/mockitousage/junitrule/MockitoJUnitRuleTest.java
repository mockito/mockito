/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.junitrule;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MockitoJUnitRuleTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    // Fixes #1578: Protect against multiple execution.
    @Rule
    public MockitoRule mockitoRule2 = mockitoRule;

    @Mock
    private Injected injected;

    @InjectMocks
    private InjectInto injectInto;

    @Test
    public void testInjectMocks() throws Exception {
        assertNotNull("Mock created", injected);
        assertNotNull("Object created", injectInto);
        assertEquals("A injected into B", injected, injectInto.getInjected());
    }

    public static class Injected { }

    public static class InjectInto {
        private Injected injected;

        public Injected getInjected() {
            return injected;
        }
    }
}
