/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.stacktrace;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockitoutil.Conditions.firstMethodInStackTrace;

public class StackTraceFilteringTest extends TestBase {
    
    @Mock private IMethods mock;

    @After
    public void resetState() {
        super.resetState();
    }
    
    @Before
    public void setup() {
        makeStackTracesClean();
    }
    
    @Test
    public void shouldFilterStackTraceOnVerify() {
        try {
            verify(mock).simpleMethod();
            fail();
        } catch (WantedButNotInvoked e) {
            Assertions.assertThat(e).has(firstMethodInStackTrace("shouldFilterStackTraceOnVerify"));
        }
    }
    
    @Test
    public void shouldFilterStackTraceOnVerifyNoMoreInteractions() {
        mock.oneArg(true);
        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (NoInteractionsWanted e) {
            Assertions.assertThat(e).has(firstMethodInStackTrace("shouldFilterStackTraceOnVerifyNoMoreInteractions"));
        }
    }
    
    @Test
    public void shouldFilterStackTraceOnVerifyZeroInteractions() {
        mock.oneArg(true);
        try {
            verifyZeroInteractions(mock);
            fail();
        } catch (NoInteractionsWanted e) {
            Assertions.assertThat(e).has(firstMethodInStackTrace("shouldFilterStackTraceOnVerifyZeroInteractions"));
        }
    }
    
    @Test
    public void shouldFilterStacktraceOnMockitoException() {
        verify(mock);
        try {
            verify(mock).oneArg(true); 
            fail();
        } catch (MockitoException expected) {
            Assertions.assertThat(expected).has(firstMethodInStackTrace("shouldFilterStacktraceOnMockitoException"));
        }
    }
    
    @Test
    public void shouldFilterStacktraceWhenVerifyingInOrder() {
        InOrder inOrder = inOrder(mock);
        mock.oneArg(true);
        mock.oneArg(false);
        
        inOrder.verify(mock).oneArg(false);
        try {
            inOrder.verify(mock).oneArg(true);
            fail();
        } catch (VerificationInOrderFailure e) {
            Assertions.assertThat(e).has(firstMethodInStackTrace("shouldFilterStacktraceWhenVerifyingInOrder"));
        }
    }
    
    @Test
    public void shouldFilterStacktraceWhenInOrderThrowsMockitoException() {
        try {
            inOrder();
            fail();
        } catch (MockitoException expected) {
            Assertions.assertThat(expected).has(firstMethodInStackTrace("shouldFilterStacktraceWhenInOrderThrowsMockitoException"));
        }
    }
    
    @Test
    public void shouldFilterStacktraceWhenInOrderVerifies() {
        try {
            InOrder inOrder = inOrder(mock);
            inOrder.verify(null);
            fail();
        } catch (MockitoException expected) {
            Assertions.assertThat(expected).has(firstMethodInStackTrace("shouldFilterStacktraceWhenInOrderVerifies"));
        }
    }
    
    @Test
    public void shouldFilterStackTraceWhenThrowingExceptionFromMockHandler() {
        try {
            when(mock.oneArg(true)).thenThrow(new Exception());
            fail();
        } catch (MockitoException expected) {
            Assertions.assertThat(expected).has(firstMethodInStackTrace("shouldFilterStackTraceWhenThrowingExceptionFromMockHandler"));
        }
    }
    
    @Test
    public void shouldShowProperExceptionStackTrace() throws Exception {
        when(mock.simpleMethod()).thenThrow(new RuntimeException());

        try {
            mock.simpleMethod();
            fail();
        } catch (RuntimeException e) {
            Assertions.assertThat(e).has(firstMethodInStackTrace("shouldShowProperExceptionStackTrace"));
        }
    }
}
