package org.mockito.junit;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.*;

public class MockitoJUnitRuleTest {

    @Rule
    public MockitoJUnitRule mockitoJUnitRule = new MockitoJUnitRule(this);

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

    @Test
    public void testThrowExceptionWhenNullTarget() throws Exception {
        try {
            new MockitoJUnitRule(null);
            fail();
        } catch (NullPointerException e) {
            assertEquals("valid message", "Mockito JUnit rule target should not be null", e.getMessage());
        }
    }

    private static class Injected {
    }

    private static class InjectInto {

        private Injected injected;

        public Injected getInjected() {
            return injected;
        }
    }
}