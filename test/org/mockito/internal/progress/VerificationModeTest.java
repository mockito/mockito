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

public class VerificationModeTest extends RequiresValidState {

    @Test
    public void shouldKnowIfNumberOfInvocationsMatters() throws Exception {
        VerificationMode mode = VerificationMode.atLeastOnce();
        assertTrue(mode.atLeastOnceMode());
        
        mode = VerificationMode.times(50);
        assertFalse(mode.atLeastOnceMode());
    }
    
    @Test
    public void shouldNotAllowCreatingModeWithNegativeNumberOfInvocations() throws Exception {
        try {
            VerificationMode.times(-50);
            fail();
        } catch (MockitoException e) {
            assertEquals("Negative value is not allowed here", e.getMessage());
        }
    }
}