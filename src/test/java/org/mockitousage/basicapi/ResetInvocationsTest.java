package org.mockitousage.basicapi;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

public class ResetInvocationsTest extends TestBase {

    @Mock
    IMethods methods;

    @Mock
    IMethods moarMethods;

    @Test
    public void reset_invocations_should_reset_only_invocations() {
        when(methods.simpleMethod()).thenReturn("return");

        methods.simpleMethod();
        verify(methods).simpleMethod();

        clearInvocations(methods);

        verifyNoMoreInteractions(methods);
        assertEquals("return", methods.simpleMethod());
    }

    @Test
    public void should_reset_invocations_on_multiple_mocks() {
        methods.simpleMethod();
        moarMethods.simpleMethod();

        clearInvocations(methods, moarMethods);

        verifyNoMoreInteractions(methods, moarMethods);
    }

    @Test(expected = NotAMockException.class)
    public void resettingNonMockIsSafe() {
        clearInvocations("");
    }

    @Test(expected = NotAMockException.class)
    public void resettingNullIsSafe() {
        clearInvocations(new Object[]{null});
    }
}
