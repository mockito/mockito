package org.mockito;

import org.junit.Test;
import org.mockito.VerifyingMode;

import static org.junit.Assert.*;


public class VerifyingModeTest {

    @Test
    public void shouldKnowIfNumberOfInvocationsMatters() throws Exception {
        VerifyingMode mode = VerifyingMode.anyTimes();
        assertFalse(mode.numberOfInvocationsMatters());
        
        mode = VerifyingMode.times(50);
        assertTrue(mode.numberOfInvocationsMatters());
    }
    
    @Test
    public void shouldNotAllowCreatingModeWithNegativeNumberOfInvocations() throws Exception {
        try {
            VerifyingMode.times(-50);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Negative value is not allowed here", e.getMessage());
        }
    }
}
