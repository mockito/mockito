/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.parents.MockitoException;
import org.mockito.internal.progress.VerificationMode;
import static org.mockito.internal.progress.VerificationMode.*;

public class VerificationModeTest extends RequiresValidState {

    //TODO add non trivial tests
    @Test
    public void shouldKnowIfNumberOfInvocationsMatters() throws Exception {
        VerificationMode mode = atLeastOnce();
        assertTrue(mode.atLeastOnceMode());
        
        mode = times(50);
        assertFalse(mode.atLeastOnceMode());
    }
    
    @Test
    public void shouldNotAllowCreatingModeWithNegativeNumberOfInvocations() throws Exception {
        try {
            times(-50);
            fail();
        } catch (MockitoException e) {
            assertEquals("Negative value is not allowed here", e.getMessage());
        }
    }
    
    @Test
    public void shouldKnowIfIsMissingMethodMode() throws Exception {
        assertTrue(atLeastOnce().missingMethodMode());
        assertTrue(times(1).missingMethodMode());
        
        assertFalse(noMoreInteractions().missingMethodMode());
        assertFalse(times(0).missingMethodMode());
        assertFalse(times(2).missingMethodMode());
    }
}