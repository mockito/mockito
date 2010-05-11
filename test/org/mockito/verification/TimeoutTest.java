package org.mockito.verification;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.concurrent.ConcurrentVerifier;
import org.mockitoutil.TestBase;

public class TimeoutTest extends TestBase {
    
    @Mock ConcurrentVerifier verifier;
    @Mock VerificationMode mode;
    @Mock VerificationDataImpl data;
    MockitoAssertionError error = new MockitoAssertionError(""); 

    @Test
    public void shouldPassWhenVerificationPasses() {
        Timeout t = new Timeout(1, 3, mode);
        
        doNothing().when(mode).verify(data);
        
        t.verify(data);
    }
    
    @Test
    public void shouldFailBecauseVerificationFails() {
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
    public void shouldPassEvenIfFirstVerificationFails() {
        Timeout t = new Timeout(1, 2, mode);
        
        doThrow(error).
        doThrow(error).
        doNothing().    
        when(mode).verify(data);
        
        t.verify(data);
    }

    @Test
    public void shouldTryToVerifyCorrectNumberOfTimes() {
        Timeout t = new Timeout(1, 4, mode);
        
        doThrow(error).when(mode).verify(data);
        
        try {
            t.verify(data);
        } catch (MockitoAssertionError e) {};
        
        verify(mode, times(5)).verify(data);
    }
}