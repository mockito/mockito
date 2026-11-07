/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.verification;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

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
    @Mock Timer timer;

    private final MockitoAssertionError error = new MockitoAssertionError("");

    @Test
    public void should_pass_when_verification_passes() {
        Timeout t = new Timeout(mode, timer);

        when(timer.isCounting()).thenReturn(true);
        doNothing().when(mode).verify(any());

        t.verify(data);

        InOrder inOrder = inOrder(timer);
        inOrder.verify(timer).start();
        inOrder.verify(timer).isCounting();
    }

    @Test
    public void should_fail_because_verification_fails() {
        Timeout t = new Timeout(mode, timer);

        when(timer.isCounting()).thenReturn(true, true, true, false);
        doThrow(error).doThrow(error).doThrow(error).when(mode).verify(any());

        try {
            t.verify(data);
            fail();
        } catch (MockitoAssertionError e) {
        }

        verify(timer, times(4)).isCounting();
    }

    @Test
    public void should_pass_even_if_first_verification_fails() {
        Timeout t = new Timeout(mode, timer);

        when(timer.isCounting()).thenReturn(true, true, true, false);
        doThrow(error).doThrow(error).doNothing().when(mode).verify(any());

        t.verify(data);
        verify(timer, times(3)).isCounting();
    }

    @Test
    public void should_try_to_verify_correct_number_of_times() {
        Timeout t = new Timeout(mode, timer);

        doThrow(error).when(mode).verify(any());
        when(timer.isCounting()).thenReturn(true, true, true, true, true, false);

        try {
            t.verify(data);
            fail();
        } catch (MockitoAssertionError e) {
        }

        // five passes while the timer is counting, plus the final pass once it has run out
        verify(mode, times(6)).verify(any());
    }

    @Test
    public void should_run_a_final_pass_once_the_timer_has_run_out() {
        Timeout t = new Timeout(mode, timer);

        when(timer.isCounting()).thenReturn(true, false);
        doThrow(error).doNothing().when(mode).verify(any());

        t.verify(data);

        verify(mode, times(2)).verify(any());
    }
}
