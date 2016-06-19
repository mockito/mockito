package org.mockitousage.junitrule;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertNotNull;

public class InvalidTargetMockitoJUnitRuleTest {

    @Rule
    public MockitoRule mockitoJUnitRule = MockitoJUnit.rule();

    @Mock
    private Injected injected;

    @InjectMocks
    private InjectInto injectInto;

    @Test
    public void shouldInjectWithInvalidReference() throws Exception {
        assertNotNull("Mock created", injected);
        assertNotNull("Test object created", injectInto);
    }

    public static class Injected { }

    public static class InjectInto {
        private Injected injected;

        public Injected getInjected() {
            return injected;
        }
    }
}
