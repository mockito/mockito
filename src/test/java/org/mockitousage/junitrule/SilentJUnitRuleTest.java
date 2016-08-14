package org.mockitousage.junitrule;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockitousage.IMethods;

import static org.mockito.Mockito.when;

public class SilentJUnitRuleTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().silent();

    @Mock private IMethods mock;

    @Test
    public void testInjectMocks() throws Exception {
        when(mock.simpleMethod(1)).thenReturn("1");
        mock.simpleMethod(2);
    }
}
