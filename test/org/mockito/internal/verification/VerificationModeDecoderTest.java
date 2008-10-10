package org.mockito.internal.verification;

import static org.mockito.internal.verification.VerificationModeImpl.*;

import org.junit.Test;
import org.mockitoutil.TestBase;


public class VerificationModeDecoderTest extends TestBase {
    
    private VerificationModeDecoder decode(VerificationMode mode) {
        return new VerificationModeDecoder(mode);
    }
    
    @Test
    public void shouldKnowIfIsMissingMethodMode() throws Exception {
        assertTrue(decode(atLeastOnce()).missingMethodMode());
        assertTrue(decode(times(1)).missingMethodMode());
        assertTrue(decode(times(10)).missingMethodMode());
        
        assertFalse(decode(atLeast(2)).missingMethodMode());
        assertFalse(decode(noMoreInteractions()).missingMethodMode());
        assertFalse(decode(times(0)).missingMethodMode());
    }
    
    @Test
    public void shouldKnowIfNumberOfInvocationsMatters() throws Exception {
        VerificationMode mode = atLeastOnce();
        assertTrue(decode(mode).atLeastMode());
        
        mode = times(50);
        assertFalse(decode(mode).atLeastMode());
    }
    
    @Test
    public void shouldKnowIfIsAtLeastMode() throws Exception {
        assertTrue(decode(atLeastOnce()).atLeastMode());
        assertTrue(decode(atLeast(10)).atLeastMode());
        
        assertFalse(decode(times(0)).atLeastMode());
        assertFalse(decode(times(10)).atLeastMode());
        assertFalse(decode(noMoreInteractions()).atLeastMode());
    }
    
    @Test
    public void shouldKnowIfMatchesActualInvocationCount() throws Exception {
        assertFalse(decode(times(1)).matchesActualCount(0));
        assertFalse(decode(times(1)).matchesActualCount(2));
        assertFalse(decode(times(10)).matchesActualCount(20));
        
        assertTrue(decode(times(1)).matchesActualCount(1));
        assertTrue(decode(times(10)).matchesActualCount(10));
    }
    
    @Test
    public void shouldKnowIfMatchesActualInvocationCountWhenAtLeastOnceMode() throws Exception {
        assertFalse(decode(atLeastOnce()).matchesActualCount(0));
        
        assertTrue(decode(atLeastOnce()).matchesActualCount(1));
        assertTrue(decode(atLeastOnce()).matchesActualCount(100));
    }
    
    @Test
    public void shouldKnowIfMatchesActualInvocationCountWhenAtLeastMode() throws Exception {
        assertFalse(decode(atLeast(10)).matchesActualCount(5));
        assertFalse(decode(atLeast(2)).matchesActualCount(1));
        
        assertTrue(decode(atLeast(10)).matchesActualCount(10));
        assertTrue(decode(atLeast(10)).matchesActualCount(15));
    }
    
    @Test
    public void shouldKnowIfTooLittleActualInvocations() throws Exception {
        assertTrue(decode(times(1)).tooLittleActualInvocations(0));
        assertTrue(decode(times(10)).tooLittleActualInvocations(9));
        
        assertFalse(decode(times(0)).tooLittleActualInvocations(0));
        assertFalse(decode(times(1)).tooLittleActualInvocations(1));
        assertFalse(decode(times(1)).tooLittleActualInvocations(2));
    }
    
    @Test
    public void shouldAtLeastModeIgnoreTooLittleActualInvocations() throws Exception {
        assertFalse(decode(atLeast(10)).tooLittleActualInvocations(5));        
        assertFalse(decode(atLeast(10)).tooLittleActualInvocations(15));        
        assertFalse(decode(atLeastOnce()).tooLittleActualInvocations(10));        
    }

    @Test
    public void shouldKnowIfTooLittleActualInvocationsInAtLeastMode() throws Exception {
        assertTrue(decode(atLeast(3)).tooLittleActualInvocationsInAtLeastMode(2));
        assertTrue(decode(atLeast(3)).tooLittleActualInvocationsInAtLeastMode(1));
        assertTrue(decode(atLeast(3)).tooLittleActualInvocationsInAtLeastMode(0));
        
        assertFalse(decode(atLeast(1)).tooLittleActualInvocationsInAtLeastMode(1));
        assertFalse(decode(atLeast(1)).tooLittleActualInvocationsInAtLeastMode(2));
    }

    @Test
    public void shouldTooLittleActualInvocationsInAtLeastModeIgnoreOtherModes() throws Exception {
        assertFalse(decode(times(10)).tooLittleActualInvocationsInAtLeastMode(5));        
        assertFalse(decode(times(10)).tooLittleActualInvocationsInAtLeastMode(15));        
    }
    
    @Test
    public void shouldKnowIfTooManyActualInvocations() throws Exception {
        assertTrue(decode(times(0)).tooManyActualInvocations(1));
        assertTrue(decode(times(10)).tooManyActualInvocations(11));
        
        assertFalse(decode(times(0)).tooManyActualInvocations(0));
        assertFalse(decode(times(1)).tooManyActualInvocations(1));
        assertFalse(decode(times(2)).tooManyActualInvocations(1));
    }
    
    @Test
    public void shouldKnowIfWantedCountIsZero() throws Exception {
        assertTrue(decode(times(0)).neverWanted());
        
        assertFalse(decode(times(1)).neverWanted());
        assertFalse(decode(times(10)).neverWanted());
        assertFalse(decode(atLeastOnce()).neverWanted());
    }
    
    @Test
    public void shouldKnowIfNeverWantedButInvoked() throws Exception {
        assertFalse(decode(times(1)).neverWantedButInvoked(0));
        assertFalse(decode(times(10)).neverWantedButInvoked(20));
        assertFalse(decode(times(0)).neverWantedButInvoked(0));
        
        assertFalse(decode(atLeastOnce()).neverWantedButInvoked(0));
        assertFalse(decode(atLeastOnce()).neverWantedButInvoked(1));
        
        assertTrue(decode(times(0)).neverWantedButInvoked(1));
        assertTrue(decode(times(0)).neverWantedButInvoked(10));
    }
}