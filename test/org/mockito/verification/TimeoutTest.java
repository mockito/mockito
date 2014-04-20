/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.verification;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.AtLeast;
import org.mockito.internal.verification.Only;
import org.mockito.internal.verification.Times;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockitoutil.TestBase;

public class TimeoutTest extends TestBase {
    
    @Mock VerificationMode mode;
    @Mock VerificationDataImpl data;
    MockitoAssertionError error = new MockitoAssertionError(""); 

    @Test
    public void should_pass_when_verification_passes() {
        Timeout t = new Timeout(1, 3, mode);
        
        doNothing().when(mode).verify(data);
        
        t.verify(data);
    }
    
    @Test
    public void should_fail_because_verification_fails() {
        Timeout t = new Timeout(1, 2, mode);
        
        doThrow(error).
        doThrow(error).
        doThrow(error).
        when(mode).verify(data);
        
        try {
            t.verify(data);
            fail();
        } catch (MockitoAssertionError e) {}
    }
    
    @Test
    public void should_pass_even_if_first_verification_fails() {
        Timeout t = new Timeout(1, 5, mode);
        
        doThrow(error).
        doThrow(error).
        doNothing().
        when(mode).verify(data);
        
        t.verify(data);
    }

    @Test
    public void should_try_to_verify_correct_number_of_times() {
        Timeout t = new Timeout(10, 50, mode);
        
        doThrow(error).when(mode).verify(data);
        
        try {
            t.verify(data);
            fail();
        } catch (MockitoAssertionError e) {}
        
        verify(mode, times(5)).verify(data);
    }
    
    @Test
    public void should_create_correctly_configured_timeout() {
        Timeout t = new Timeout(25, 50, mode);
        
        assertTimeoutCorrectlyConfigured(t.atLeastOnce(), Timeout.class, 50, 25, AtLeast.class);
        assertTimeoutCorrectlyConfigured(t.atLeast(5), Timeout.class, 50, 25, AtLeast.class);
        assertTimeoutCorrectlyConfigured(t.times(5), Timeout.class, 50, 25, Times.class);
        assertTimeoutCorrectlyConfigured(t.only(), Timeout.class, 50, 25, Only.class);
    }
    
    private void assertTimeoutCorrectlyConfigured(VerificationMode t, Class<?> expectedType, long expectedTimeout, long expectedPollingPeriod, Class<?> expectedDelegateType) {
        assertEquals(expectedType, t.getClass());
        assertEquals(expectedTimeout, ((Timeout) t).wrappedVerification.getDuration());
        assertEquals(expectedPollingPeriod, ((Timeout) t).wrappedVerification.getPollingPeriod());
        assertEquals(expectedDelegateType, ((Timeout) t).wrappedVerification.getDelegate().getClass());
    }
}