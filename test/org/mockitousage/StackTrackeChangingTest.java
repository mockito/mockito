/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.util.ExtraMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.TestBase;
import org.mockito.exceptions.verification.ArgumentsAreDifferentException;

public class StackTrackeChangingTest extends TestBase {
    
    private IMethods mock;

    @Before
    public void setup() {
        mock = Mockito.mock(IMethods.class);
    }
    
    private void simpleMethodOnAMock() {
        mock.simpleMethod("blah");
    }
    
    @Test
    public void shouldShowActualInvocationAsExceptionCause() {
        simpleMethodOnAMock();
        try {
            verifySimpleMethodOnAMock();
            fail();
        } catch (ArgumentsAreDifferentException e) {
            //TODO hasOnlyThoseMethods stuff
            assertThat(e, hasMethodInStackTraceAt(0, "verifySimpleMethodOnAMock"));
            assertThat(e, hasMethodInStackTraceAt(1, "shouldShowActualInvocationAsExceptionCause"));
            assertThat(e.getCause(), hasMethodInStackTraceAt(0, "simpleMethodOnAMock"));
            assertThat(e.getCause(), hasMethodInStackTraceAt(1, "shouldShowActualInvocationAsExceptionCause"));
        }
    }

    private void verifySimpleMethodOnAMock() {
        verify(mock).simpleMethod();        
    }
}
