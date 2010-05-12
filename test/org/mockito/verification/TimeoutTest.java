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
            fail();
        } catch (MockitoAssertionError e) {};
        
        verify(mode, times(5)).verify(data);
    }
    
    @Test
    public void shouldCreateCorrectType() {
        Timeout t = new Timeout(25, 50, mode);
        
        assertCorrectMode(t.atLeastOnce(), Timeout.class, 50, 25, AtLeast.class);
        assertCorrectMode(t.atLeast(5), Timeout.class, 50, 25, AtLeast.class);
        assertCorrectMode(t.times(5), Timeout.class, 50, 25, Times.class);
        assertCorrectMode(t.never(), Timeout.class, 50, 25, Times.class);
        assertCorrectMode(t.only(), Timeout.class, 50, 25, Only.class);
        assertCorrectMode(t.atMost(10), Timeout.class, 50, 25, AtMost.class);
    }
    
    private void assertCorrectMode(VerificationMode t, Class expectedType, int expectedTimeout, int expectedTreshold, Class expectedDelegateType) {
        assertEquals(expectedType, t.getClass());
        assertEquals(expectedTimeout, ((Timeout) t).impl.getTimeout());
        assertEquals(expectedTreshold, ((Timeout) t).impl.getTreshhold());
        assertEquals(expectedDelegateType, ((Timeout) t).impl.getDelegate().getClass());
    }
}