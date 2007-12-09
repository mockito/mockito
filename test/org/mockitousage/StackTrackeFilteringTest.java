/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.util.ExtraMatchers.hasFirstMethodInStackTrace;

import org.junit.*;
import org.mockito.*;
import org.mockito.exceptions.parents.MockitoException;
import org.mockito.exceptions.verification.VerificationError;
import org.mockito.internal.StateResetter;
import org.mockito.util.RequiresValidState;

public class StackTrackeFilteringTest extends RequiresValidState {
    
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
        } catch (VerificationError expected) {
            assertThat(expected, hasFirstMethodInStackTrace("shouldFilterStackTraceOnVerify"));
        }
    }
    
    @Test
    public void shouldFilterStackTraceOnVerifyNoMoreInteractions() {
        mock.oneArg(true);
        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (VerificationError expected) {
            assertThat(expected, hasFirstMethodInStackTrace("shouldFilterStackTraceOnVerifyNoMoreInteractions"));
        }
    }
    
    @Test
    public void shouldFilterStackTraceOnVerifyZeroInteractions() {
        mock.oneArg(true);
        try {
            verifyZeroInteractions(mock);
            fail();
        } catch (VerificationError expected) {
            assertThat(expected, hasFirstMethodInStackTrace("shouldFilterStackTraceOnVerifyZeroInteractions"));
        }
    }
    
    @Test
    public void shouldFilterStacktraceOnMockitoException() {
        verify(mock);
        try {
            verify(mock).oneArg(true); 
            fail();
        } catch (MockitoException expected) {
            assertThat(expected, hasFirstMethodInStackTrace("shouldFilterStacktraceOnMockitoException"));
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
        } catch (VerificationError expected) {
            assertThat(expected, hasFirstMethodInStackTrace("shouldFilterStacktraceWhenStrictlyVerifying"));
        }
    }
    
    @Test
    public void shouldFilterStacktraceWhenStrictlyThrowsMockitoException() {
        try {
            createStrictOrderVerifier();
            fail();
        } catch (MockitoException expected) {
            assertThat(expected, hasFirstMethodInStackTrace("shouldFilterStacktraceWhenStrictlyThrowsMockitoException"));
        }
    }
    
    @Test
    public void shouldFilterStacktraceWhenStrictlyVerifies() {
        try {
            Strictly strictly = createStrictOrderVerifier(mock);
            strictly.verify(null);
            fail();
        } catch (MockitoException expected) {
            assertThat(expected, hasFirstMethodInStackTrace("shouldFilterStacktraceWhenStrictlyVerifies"));
        }
    }
    
    @Test
    public void shouldFilterStackTraceWhenThrowingExceptionFromControl() {
        try {
            stub(mock.oneArg(true)).andThrows(new Exception());
            fail();
        } catch (MockitoException expected) {
            assertThat(expected, hasFirstMethodInStackTrace("shouldFilterStackTraceWhenThrowingExceptionFromControl"));
        }
    }
    
    @Test
    public void shouldShowProperExceptionStackTrace() throws Exception {
        stub(mock.simpleMethod()).andThrows(new RuntimeException());

        try {
            mock.simpleMethod();
            fail();
        } catch (RuntimeException e) {
            assertThat(e, hasFirstMethodInStackTrace("shouldShowProperExceptionStackTrace"));
        }
    }
}
