/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.verification;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockitoutil.TestBase;

public class TimeoutTest extends TestBase {
    
    @Mock VerificationMode mode;
    @Mock VerificationDataImpl data;
    @Mock
    Timer timer;
    MockitoAssertionError error = new MockitoAssertionError(""); 

    @Test
    public void should_pass_when_verification_passes() {
        final Timeout t = new Timeout(1, mode, timer);

        when(timer.isCounting()).thenReturn(true);
        doNothing().when(mode).verify(data);

        t.verify(data);

        final InOrder inOrder = inOrder(timer);
        inOrder.verify(timer).start();
        inOrder.verify(timer).isCounting();
    }

    @Test
    public void should_fail_because_verification_fails() {
        final Timeout t = new Timeout(1, mode, timer);

        when(timer.isCounting()).thenReturn(true, true, true, false);
        doThrow(error).
        doThrow(error).
        doThrow(error).
        when(mode).verify(data);
        
        try {
            t.verify(data);
            fail();
        } catch (final MockitoAssertionError e) {}

        verify(timer, times(4)).isCounting();
    }
    
    @Test
    public void should_pass_even_if_first_verification_fails() {
        final Timeout t = new Timeout(1, mode, timer);

        when(timer.isCounting()).thenReturn(true, true, true, false);
        doThrow(error).
        doThrow(error).
        doNothing().
        when(mode).verify(data);
        
        t.verify(data);
        verify(timer, times(3)).isCounting();
    }

    @Test
    public void should_try_to_verify_correct_number_of_times() {
        final Timeout t = new Timeout(10, mode, timer);
        
        doThrow(error).when(mode).verify(data);
        when(timer.isCounting()).thenReturn(true, true, true, true, true, false);

        try {
            t.verify(data);
            fail();
        } catch (final MockitoAssertionError e) {}

        verify(mode, times(5)).verify(data);
    }

}