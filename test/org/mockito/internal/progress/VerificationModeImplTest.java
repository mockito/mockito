/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import static java.util.Arrays.*;
import static org.mockito.internal.progress.VerificationModeImpl.*;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockitoutil.TestBase;

public class VerificationModeImplTest extends TestBase {

    @Test
    public void shouldKnowIfNumberOfInvocationsMatters() throws Exception {
        VerificationModeImpl mode = atLeastOnce();
        assertTrue(mode.atLeastMode());
        
        mode = times(50);
        assertFalse(mode.atLeastMode());
    }
    
    @Test
    public void shouldNotAllowNegativeNumberOfInvocations() throws Exception {
        try {
            times(-50);
            fail();
        } catch (MockitoException e) {
            assertEquals("Negative value is not allowed here", e.getMessage());
        }
    }

    @Test
    public void shouldNotAllowNegativeNumberOfMinimumInvocations() throws Exception {
        try {
            atLeast(-50);
            fail();
        } catch (MockitoException e) {
            assertEquals("Negative value or zero are not allowed here", e.getMessage());
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
    public void shouldKnowIfIsAtLeastMode() throws Exception {
        assertTrue(atLeastOnce().atLeastMode());
        assertTrue(atLeast(10).atLeastMode());
        
        assertFalse(times(0).atLeastMode());
        assertFalse(times(2).atLeastMode());
        assertFalse(noMoreInteractions().atLeastMode());
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
    public void shouldKnowIfMatchesActualInvocationCountWhenAtLeastMode() throws Exception {
        assertFalse(atLeast(10).matchesActualCount(5));
        assertFalse(atLeast(2).matchesActualCount(1));
        
        assertTrue(atLeast(10).matchesActualCount(10));
        assertTrue(atLeast(10).matchesActualCount(15));
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
    public void shouldAtLeastModeIgnoreTooLittleActualInvocations() throws Exception {
        assertFalse(atLeast(10).tooLittleActualInvocations(5));        
        assertFalse(atLeast(10).tooLittleActualInvocations(15));        
        assertFalse(atLeastOnce().tooLittleActualInvocations(10));        
    }

    @Test
    public void shouldKnowIfTooLittleActualInvocationsInAtLeastMode() throws Exception {
        assertTrue(atLeast(3).tooLittleActualInvocationsInAtLeastMode(2));
        assertTrue(atLeast(3).tooLittleActualInvocationsInAtLeastMode(1));
        assertTrue(atLeast(3).tooLittleActualInvocationsInAtLeastMode(0));
        
        assertFalse(atLeast(1).tooLittleActualInvocationsInAtLeastMode(1));
        assertFalse(atLeast(1).tooLittleActualInvocationsInAtLeastMode(2));
    }

    @Test
    public void shouldTooLittleActualInvocationsInAtLeastModeIgnoreOtherModes() throws Exception {
        assertFalse(times(10).tooLittleActualInvocationsInAtLeastMode(5));        
        assertFalse(times(10).tooLittleActualInvocationsInAtLeastMode(15));        
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