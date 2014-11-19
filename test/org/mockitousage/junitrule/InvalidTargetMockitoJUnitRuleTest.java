package org.mockitousage.junitrule;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRule;

import static org.junit.Assert.assertNotNull;
import static org.mockitousage.junitrule.MockitoJUnitRuleTest.InjectInto;
import static org.mockitousage.junitrule.MockitoJUnitRuleTest.Injected;

public class InvalidTargetMockitoJUnitRuleTest {

    @Rule
    public MockitoJUnitRule mockitoJUnitRule = new MockitoJUnitRule("asdf");

    @Mock
    private Injected injected;

    @InjectMocks
    private InjectInto injectInto;

    @Test
    public void shouldInjectWithInvalidReference() throws Exception {
        assertNotNull("Mock created", injected);
        assertNotNull("Test object created", injectInto);
    }
}
