/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.util.ExtraMatchers.hasMethodInStackTraceAt;

import org.junit.*;
import org.mockito.Mockito;
import org.mockito.exceptions.VerificationError;
import org.mockito.util.RequiresValidState;

public class StackTrackeChangingTest extends RequiresValidState {
    
    private IMethods mock;

    @Before
    public void setup() {
        mock = Mockito.mock(IMethods.class);
    }
    
    private void simpleMethodOnAMock() {
        mock.simpleMethod("blah");
    }
    
    @Test
    public void shouldMergeInvocationStackTraceAndExceptionStackTrace() {
        simpleMethodOnAMock();
        try {
            verifySimpleMethodOnAMock();
            fail();
        } catch (VerificationError e) {
            assertThat(e, hasMethodInStackTraceAt(0, "verifySimpleMethodOnAMock"));
            assertThat(e, hasMethodInStackTraceAt(1, "shouldMergeInvocationStackTraceAndExceptionStackTrace"));
            assertThat(e, hasMethodInStackTraceAt(2, "_below_is_actual_invocation_stack_trace_"));
            assertThat(e, hasMethodInStackTraceAt(3, "simpleMethodOnAMock"));
            assertThat(e, hasMethodInStackTraceAt(4, "shouldMergeInvocationStackTraceAndExceptionStackTrace"));
        }
    }

    private void verifySimpleMethodOnAMock() {
        verify(mock).simpleMethod();        
    }
}
