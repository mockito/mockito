package org.mockito.verification;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.AtLeast;
import org.mockito.internal.verification.AtMost;
import org.mockito.internal.verification.Only;
import org.mockito.internal.verification.Times;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class TimeoutTest extends TestBase {
    
    @Mock VerificationMode mode;
    @Mock VerificationDataImpl data;
    MockitoAssertionError error = new MockitoAssertionError(""); 

    @Test
    public void shouldPassWhenVerificationPasses() {
        VerificationWithTimeout t = new VerificationWithTimeout(1, 3, mode);
        
        doNothing().when(mode).verify(data);
        
        t.verify(data);
    }
    
    @Test
    public void shouldFailBecauseVerificationFails() {
        VerificationWithTimeout t = new VerificationWithTimeout(1, 2, mode);
        
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
        VerificationWithTimeout t = new VerificationWithTimeout(1, 2, mode);
        
        doThrow(error).
        doThrow(error).
        doNothing().    
        when(mode).verify(data);
        
        t.verify(data);
    }

    @Test
    public void shouldTryToVerifyCorrectNumberOfTimes() {
        VerificationWithTimeout t = new VerificationWithTimeout(1, 4, mode);
        
        doThrow(error).when(mode).verify(data);
        
        try {
            t.verify(data);
        } catch (MockitoAssertionError e) {};
        
        verify(mode, times(5)).verify(data);
    }
    
    @Test
    public void shouldCreateCorrectType() {
        VerificationWithTimeout t = new VerificationWithTimeout(25, 50, mode);
        
        assertCorrectMode(t.atLeastOnce(), VerificationWithTimeout.class, 50, 25, AtLeast.class);
        assertCorrectMode(t.atLeast(5), VerificationWithTimeout.class, 50, 25, AtLeast.class);
        assertCorrectMode(t.times(5), VerificationWithTimeout.class, 50, 25, Times.class);
        assertCorrectMode(t.never(), VerificationWithTimeout.class, 50, 25, Times.class);
        assertCorrectMode(t.only(), VerificationWithTimeout.class, 50, 25, Only.class);
        assertCorrectMode(t.atMost(10), VerificationWithTimeout.class, 50, 25, AtMost.class);
    }
    
    private void assertCorrectMode(VerificationMode t, Class expectedType, int expectedTimeout, int expectedTreshold, Class expectedDelegateType) {
        assertEquals(expectedType, t.getClass());
        assertEquals(expectedTimeout, ((VerificationWithTimeout) t).impl.getTimeout());
        assertEquals(expectedTreshold, ((VerificationWithTimeout) t).impl.getTreshhold());
        assertEquals(expectedDelegateType, ((VerificationWithTimeout) t).impl.getDelegate().getClass());
    }
}