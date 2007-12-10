/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.parents.MockitoException;

public class VerifyingModeTest extends RequiresValidState {

    @Test
    public void shouldKnowIfNumberOfInvocationsMatters() throws Exception {
        VerifyingMode mode = VerifyingMode.atLeastOnce();
        assertTrue(mode.atLeastOnceMode());
        
        mode = VerifyingMode.times(50);
        assertFalse(mode.atLeastOnceMode());
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