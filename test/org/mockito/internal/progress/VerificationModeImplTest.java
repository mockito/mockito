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
import static org.mockito.internal.progress.VerificationModeImpl.atLeastOnce;
import static org.mockito.internal.progress.VerificationModeImpl.noMoreInteractions;
import static org.mockito.internal.progress.VerificationModeImpl.inOrder;
import static org.mockito.internal.progress.VerificationModeImpl.times;

import org.junit.Test;
import org.mockito.TestBase;
import org.mockito.exceptions.base.MockitoException;

public class VerificationModeImplTest extends TestBase {

    @Test
    public void shouldKnowIfNumberOfInvocationsMatters() throws Exception {
        VerificationModeImpl mode = atLeastOnce();
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
        assertTrue(times(10).missingMethodMode());
        
        assertFalse(noMoreInteractions().missingMethodMode());
        assertFalse(times(0).missingMethodMode());
    }
    
    @Test
    public void shouldKnowIfIsMissingMethodInOrderMode() throws Exception {
        assertTrue(inOrder(null, asList("mock")).missingMethodInOrderMode());
        assertTrue(inOrder(1, asList("mock")).missingMethodInOrderMode());
        assertTrue(inOrder(10, asList("mock")).missingMethodInOrderMode());
        
        assertFalse(times(10).missingMethodInOrderMode());
        assertFalse(noMoreInteractions().missingMethodInOrderMode());
        assertFalse(times(0).missingMethodInOrderMode());
    }
    
    @Test
    public void shouldKnowIfIsInOrderMode() throws Exception {
        assertTrue(inOrder(1, asList(new Object())).inOrderMode());
        
        assertFalse(times(0).inOrderMode());
        assertFalse(times(2).inOrderMode());
        assertFalse(atLeastOnce().inOrderMode());
        assertFalse(noMoreInteractions().inOrderMode());
    }
    
    @Test
    public void shouldKnowIfIsAtLeastOnceMode() throws Exception {
        assertTrue(atLeastOnce().atLeastOnceMode());
        
        assertFalse(times(0).atLeastOnceMode());
        assertFalse(times(2).atLeastOnceMode());
        assertFalse(noMoreInteractions().atLeastOnceMode());
    }
    
    @Test
    public void shouldKnowIfMatchesActualInvocationCount() throws Exception {
        assertFalse(times(1).matchesActualCount(0));
        assertFalse(times(1).matchesActualCount(2));
        assertFalse(times(100).matchesActualCount(200));
        
        assertTrue(times(1).matchesActualCount(1));
        assertTrue(times(100).matchesActualCount(100));
    }
    
    @Test
    public void shouldKnowIfMatchesActualInvocationCountWhenAtLeastOnceMode() throws Exception {
        assertFalse(atLeastOnce().matchesActualCount(0));
        
        assertTrue(atLeastOnce().matchesActualCount(1));
        assertTrue(atLeastOnce().matchesActualCount(100));
    }
    
    @Test
    public void shouldKnowIfTooLittleActualInvocations() throws Exception {
        assertTrue(times(1).tooLittleActualInvocations(0));
        assertTrue(times(100).tooLittleActualInvocations(99));
        
        assertFalse(times(0).tooLittleActualInvocations(0));
        assertFalse(times(1).tooLittleActualInvocations(1));
        assertFalse(times(1).tooLittleActualInvocations(2));
    }
    
    @Test
    public void shouldKnowIfTooManyActualInvocations() throws Exception {
        assertTrue(times(0).tooManyActualInvocations(1));
        assertTrue(times(99).tooManyActualInvocations(100));
        
        assertFalse(times(0).tooManyActualInvocations(0));
        assertFalse(times(1).tooManyActualInvocations(1));
        assertFalse(times(2).tooManyActualInvocations(1));
    }
    
    @Test
    public void shouldKnowIfWantedCountIsZero() throws Exception {
        assertTrue(times(0).neverWanted());
        
        assertFalse(times(1).neverWanted());
        assertFalse(times(20).neverWanted());
        assertFalse(atLeastOnce().neverWanted());
    }
    
    @Test
    public void shouldKnowIfExactNumberOfInvocationsMode() throws Exception {
        assertTrue(times(0).exactNumberOfInvocationsMode());
        assertTrue(times(1).exactNumberOfInvocationsMode());
        assertTrue(atLeastOnce().exactNumberOfInvocationsMode());
        
        assertFalse(noMoreInteractions().exactNumberOfInvocationsMode());
        assertFalse(inOrder(1, asList(new Object())).exactNumberOfInvocationsMode());
    }
    
    @Test
    public void shouldKnowIfNeverWantedButInvoked() throws Exception {
        assertFalse(times(1).neverWantedButInvoked(0));
        assertFalse(times(10).neverWantedButInvoked(20));
        assertFalse(times(0).neverWantedButInvoked(0));
        
        assertFalse(atLeastOnce().neverWantedButInvoked(0));
        assertFalse(atLeastOnce().neverWantedButInvoked(1));
        
        assertTrue(times(0).neverWantedButInvoked(1));
        assertTrue(times(0).neverWantedButInvoked(10));
    }
}