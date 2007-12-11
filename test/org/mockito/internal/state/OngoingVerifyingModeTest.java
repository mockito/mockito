/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.state;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.parents.MockitoException;
import org.mockito.internal.state.OngoingVerifyingMode;

public class OngoingVerifyingModeTest extends RequiresValidState {

    @Test
    public void shouldKnowIfNumberOfInvocationsMatters() throws Exception {
        OngoingVerifyingMode mode = OngoingVerifyingMode.atLeastOnce();
        assertTrue(mode.atLeastOnceMode());
        
        mode = OngoingVerifyingMode.times(50);
        assertFalse(mode.atLeastOnceMode());
    }
    
    @Test
    public void shouldNotAllowCreatingModeWithNegativeNumberOfInvocations() throws Exception {
        try {
            OngoingVerifyingMode.times(-50);
            fail();
        } catch (MockitoException e) {
            assertEquals("Negative value is not allowed here", e.getMessage());
        }
    }
}