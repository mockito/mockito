package org.mockito.internal;

import org.junit.Test;
import org.mockito.exceptions.MockitoException;
import org.mockito.internal.VerifyingMode;

import static org.junit.Assert.*;

public class VerifyingModeTest {

    @Test
    public void shouldKnowIfNumberOfInvocationsMatters() throws Exception {
        VerifyingMode mode = VerifyingMode.atLeastOnce();
        assertTrue(mode.invokedAtLeastOnce());
        
        mode = VerifyingMode.times(50);
        assertFalse(mode.invokedAtLeastOnce());
    }
    
    @Test
    public void shouldNotAllowCreatingModeWithNegativeNumberOfInvocations() throws Exception {
        try {
            VerifyingMode.times(-50);
            fail();
        } catch (MockitoException e) {
            assertEquals("Negative value is not allowed here", e.getMessage());
        }
    }
}