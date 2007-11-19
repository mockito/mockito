package org.mockito.usage.verification;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.mockito.Mockito;
import org.mockito.exceptions.VerificationAssertionError;
import org.mockito.usage.IMethods;
import static org.mockito.util.ExtraMatchers.*;

public class StackTrackeFilteringWhenVerificationFailsTest {
    
    private IMethods mock;

    @Before
    public void setup() {
        mock = Mockito.mock(IMethods.class);
    }
    
    @Test
    public void shouldFilterStackTraceOnVerify() {
        try {
            verify(mock).simpleMethod();
        } catch (VerificationAssertionError expected) {
            assertThat(expected, firstMethodOnStackEqualsTo("shouldFilterStackTraceOnVerify"));
            
            StackTraceElement[] unfilteredStackTrace = expected.getUnfilteredStackTrace();
            assertEquals("createNotInvokedError", unfilteredStackTrace[0].getMethodName());
        }
    }
    
    @Test
    public void shouldFilterStackTraceOnVerifyNoMoreInteractions() {
        mock.oneArg(true);
        try {
            verifyNoMoreInteractions(mock);
        } catch (VerificationAssertionError expected) {
            assertThat(expected, firstMethodOnStackEqualsTo("shouldFilterStackTraceOnVerifyNoMoreInteractions"));
            
            StackTraceElement[] unfilteredStackTrace = expected.getUnfilteredStackTrace();
            assertEquals("createNoMoreInteractionsError", unfilteredStackTrace[0].getMethodName());
        }
    }
    
    @Test
    public void shouldFilterStackTraceOnVerifyZeroInteractions() {
        mock.oneArg(true);
        try {
            verifyZeroInteractions(mock);
        } catch (VerificationAssertionError expected) {
            assertThat(expected, firstMethodOnStackEqualsTo("shouldFilterStackTraceOnVerifyZeroInteractions"));
            
            StackTraceElement[] unfilteredStackTrace = expected.getUnfilteredStackTrace();
            assertEquals("createNoMoreInteractionsError", unfilteredStackTrace[0].getMethodName());
        }
    }
}
