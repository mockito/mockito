/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.internal.progress.VerificationMode.atLeastOnce;
import static org.mockito.internal.progress.VerificationMode.noMoreInteractions;
import static org.mockito.internal.progress.VerificationMode.strict;
import static org.mockito.internal.progress.VerificationMode.times;

import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.base.MockitoException;

public class VerificationModeTest extends RequiresValidState {

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
    
    @Test
    public void shouldKnowIfIsExactNumberOfInvocationsMode() throws Exception {
        assertTrue(times(0).exactNumberOfInvocationsMode());
        assertTrue(times(1).exactNumberOfInvocationsMode());
        assertTrue(times(2).exactNumberOfInvocationsMode());
        
        assertFalse(noMoreInteractions().exactNumberOfInvocationsMode());
        assertFalse(atLeastOnce().exactNumberOfInvocationsMode());
    }
    
    @Test
    public void shouldKnowIfWantedCountIsZero() throws Exception {
        assertTrue(times(0).wantedCountIsZero());
        
        assertFalse(times(1).wantedCountIsZero());
        assertFalse(times(2).wantedCountIsZero());
        assertFalse(atLeastOnce().wantedCountIsZero());
    }
    
    @Test
    public void shouldKnowIfIsStrict() throws Exception {
        assertTrue(strict(1, asList(new Object())).strictMode());
        
        assertFalse(times(0).strictMode());
        assertFalse(times(2).strictMode());
        assertFalse(atLeastOnce().strictMode());
        assertFalse(noMoreInteractions().strictMode());
    }
    
    @Test
    public void shouldKnowIfIsAtLeastOnceMode() throws Exception {
        assertTrue(atLeastOnce().atLeastOnceMode());
        
        assertFalse(times(0).atLeastOnceMode());
        assertFalse(times(2).atLeastOnceMode());
        assertFalse(noMoreInteractions().atLeastOnceMode());
    }
}