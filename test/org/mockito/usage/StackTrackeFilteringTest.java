/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.usage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.mockito.*;
import org.mockito.exceptions.*;
import org.mockito.internal.StateResetter;

import static org.mockito.util.ExtraMatchers.*;

public class StackTrackeFilteringTest {
    
    private IMethods mock;

    @After
    public void resetState() {
        StateResetter.reset();
    }
    
    @Before
    public void setup() {
        resetState();
        mock = Mockito.mock(IMethods.class);
    }
    
    @Test
    public void shouldFilterStackTraceOnVerify() {
        try {
            verify(mock).simpleMethod();
            fail();
        } catch (VerificationAssertionError expected) {
            assertThat(expected, firstMethodOnStackEqualsTo("shouldFilterStackTraceOnVerify"));
            
            StackTraceElement[] unfilteredStackTrace = expected.getUnfilteredStackTrace();
            assertEquals("reportMissingInvocationError", unfilteredStackTrace[0].getMethodName());
        }
    }
    
    @Test
    public void shouldFilterStackTraceOnVerifyNoMoreInteractions() {
        mock.oneArg(true);
        try {
            verifyNoMoreInteractions(mock);
            fail();
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
            fail();
        } catch (VerificationAssertionError expected) {
            assertThat(expected, firstMethodOnStackEqualsTo("shouldFilterStackTraceOnVerifyZeroInteractions"));
            
            StackTraceElement[] unfilteredStackTrace = expected.getUnfilteredStackTrace();
            assertEquals("verifyNoMoreInteractions", unfilteredStackTrace[0].getMethodName());
        }
    }
    
    @Test
    public void shouldFilterStacktraceOnMockitoException() {
        verify(mock);
        try {
            verify(mock).oneArg(true); 
            fail();
        } catch (MockitoException expected) {
            assertThat(expected, firstMethodOnStackEqualsTo("shouldFilterStacktraceOnMockitoException"));
            
            StackTraceElement[] unfilteredStackTrace = expected.getUnfilteredStackTrace();
            assertEquals("checkForUnfinishedVerification", unfilteredStackTrace[0].getMethodName());
        }
    }
    
    @Test
    public void shouldFilterStacktraceWhenStrictlyVerifying() {
        Strictly strictly = createStrictOrderVerifier(mock);
        mock.oneArg(true);
        mock.oneArg(false);
        try {
            strictly.verify(mock).oneArg(false); 
            fail();
        } catch (StrictVerificationError expected) {
            assertThat(expected, firstMethodOnStackEqualsTo("shouldFilterStacktraceWhenStrictlyVerifying"));
            
            StackTraceElement[] unfilteredStackTrace = expected.getUnfilteredStackTrace();
            assertEquals("checkOrderOfInvocations", unfilteredStackTrace[0].getMethodName());
        }
    }
}
