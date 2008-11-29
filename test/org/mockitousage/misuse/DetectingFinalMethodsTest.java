package org.mockitousage.misuse;
import static org.mockito.Mockito.*;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.MissingMethodInvocationException;
import org.mockito.exceptions.misusing.UnfinishedVerificationException;
import org.mockito.internal.util.MockUtil;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class DetectingFinalMethodsTest extends TestBase {
    
    class WithFinal {
        final int foo() {
            return 0;
        }
    }
    
    @Mock private WithFinal withFinal;
    @Mock private IMethods mock;

    @Test
    public void shouldFailWithUnfinishedVerification() {
        withFinal = mock(WithFinal.class);
        verify(withFinal).foo();
        try {
            verify(withFinal).foo();
            fail();
        } catch (UnfinishedVerificationException e) {}
    }

    @Test
    public void shouldFailWithUnfinishedStubbing() {
        withFinal = mock(WithFinal.class);
        MockUtil.getMockHandler(withFinal);
        try {
            when(withFinal.foo()).thenReturn(null);
            fail();
        } catch (MissingMethodInvocationException e) {}
    }
}