package org.mockitousage.junitrule;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RuleTestWithParameterConstructorTest {

	 @Rule
	 public MockitoRule mockitoJUnitRule = MockitoJUnit.rule();

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

    public static class Injected {
    }

    public static class InjectInto {

        private Injected injected;

        public Injected getInjected() {
            return injected;
        }
    }
}