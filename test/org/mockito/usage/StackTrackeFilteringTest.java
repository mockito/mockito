package org.mockito.usage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.mockito.Mockito;
import org.mockito.exceptions.*;

import static org.mockito.util.ExtraMatchers.*;

public class StackTrackeFilteringTest {
    
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
            assertEquals("verify", unfilteredStackTrace[0].getMethodName());
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
            assertEquals("verifyNoMoreInteractions", unfilteredStackTrace[0].getMethodName());
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
            assertEquals("verifyNoMoreInteractions", unfilteredStackTrace[0].getMethodName());
        }
    }
    
    @Test
    public void shouldFilterStacktraceOnUnfinishedVerification() {
        verify(mock);
        try {
            verify(mock).oneArg(true); 
            fail();
        } catch (MockitoException expected) {
            assertThat(expected, firstMethodOnStackEqualsTo("shouldFilterStacktraceOnUnfinishedVerification"));
            
            StackTraceElement[] unfilteredStackTrace = expected.getUnfilteredStackTrace();
            assertEquals("checkForUnfinishedVerification", unfilteredStackTrace[0].getMethodName());
        }
    }
    
    //TODO add all other stack filtering stuff
}
