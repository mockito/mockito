package org.mockito.rules;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertNull;
import static org.mockito.rules.MockitoJUnitRuleTest.InjectInto;
import static org.mockito.rules.MockitoJUnitRuleTest.Injected;

public class InvalidTargetMockitoJUnitRuleTest {
    @Rule
    public MockitoJUnitRule mockitoJUnitRule = new MockitoJUnitRule("asdf");

    @Mock
    private Injected injected;

    @InjectMocks
    private InjectInto injectInto;

    @Test
    public void testInvalidReference() throws Exception {
        assertNull("Mock not created", injected);
        assertNull("Test object not created", injectInto);
    }
}
