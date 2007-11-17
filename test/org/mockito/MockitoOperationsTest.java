package org.mockito;

import org.junit.Test;
import org.mockito.*;
import org.mockito.internal.*;

import static org.junit.Assert.*;

public class MockitoOperationsTest {

    @Test
    public void shouldSwitchVerifyingMode() throws Exception {
        MockitoOperations.reportVerifyingMode(VerifyingMode.anyTimes());
        
        assertTrue(MockitoOperations.mockVerificationScenario());
        
        MockitoOperations.reportVerifyingMode(null);
        
        assertFalse(MockitoOperations.mockVerificationScenario());
        
        MockitoOperations.reportVerifyingMode(VerifyingMode.times(100));
        
        assertTrue(MockitoOperations.mockVerificationScenario());
        
        assertEquals(100, MockitoOperations.removeVerifyingMode().getExactNumberOfInvocations());
        
        assertFalse(MockitoOperations.mockVerificationScenario());
    }
}