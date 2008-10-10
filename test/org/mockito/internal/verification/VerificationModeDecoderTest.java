package org.mockito.internal.verification;

import org.junit.Test;
import org.mockitoutil.TestBase;


public class VerificationModeDecoderTest extends TestBase {
    
    private VerificationModeDecoder decode(MockitoVerificationMode mode) {
        return new VerificationModeDecoder(mode);
    }
    
    @Test
    public void shouldKnowIfIsMissingMethodMode() throws Exception {
        assertTrue(decode(VerificationModeFactory.atLeastOnce()).missingMethodMode());
        assertTrue(decode(VerificationModeFactory.times(1)).missingMethodMode());
        assertTrue(decode(VerificationModeFactory.times(10)).missingMethodMode());
        
        assertFalse(decode(VerificationModeFactory.atLeast(2)).missingMethodMode());
        assertFalse(decode(VerificationModeFactory.times(0)).missingMethodMode());
    }
    
    @Test
    public void shouldKnowIfNumberOfInvocationsMatters() throws Exception {
        MockitoVerificationMode mode = VerificationModeFactory.atLeastOnce();
        assertTrue(decode(mode).atLeastMode());
        
        mode = VerificationModeFactory.times(50);
        assertFalse(decode(mode).atLeastMode());
    }
    
    @Test
    public void shouldKnowIfIsAtLeastMode() throws Exception {
        assertTrue(decode(VerificationModeFactory.atLeastOnce()).atLeastMode());
        assertTrue(decode(VerificationModeFactory.atLeast(10)).atLeastMode());
        
        assertFalse(decode(VerificationModeFactory.times(0)).atLeastMode());
        assertFalse(decode(VerificationModeFactory.times(10)).atLeastMode());
    }
    
    @Test
    public void shouldKnowIfMatchesActualInvocationCount() throws Exception {
        assertFalse(decode(VerificationModeFactory.times(1)).matchesActualCount(0));
        assertFalse(decode(VerificationModeFactory.times(1)).matchesActualCount(2));
        assertFalse(decode(VerificationModeFactory.times(10)).matchesActualCount(20));
        
        assertTrue(decode(VerificationModeFactory.times(1)).matchesActualCount(1));
        assertTrue(decode(VerificationModeFactory.times(10)).matchesActualCount(10));
    }
    
    @Test
    public void shouldKnowIfMatchesActualInvocationCountWhenAtLeastOnceMode() throws Exception {
        assertFalse(decode(VerificationModeFactory.atLeastOnce()).matchesActualCount(0));
        
        assertTrue(decode(VerificationModeFactory.atLeastOnce()).matchesActualCount(1));
        assertTrue(decode(VerificationModeFactory.atLeastOnce()).matchesActualCount(100));
    }
    
    @Test
    public void shouldKnowIfMatchesActualInvocationCountWhenAtLeastMode() throws Exception {
        assertFalse(decode(VerificationModeFactory.atLeast(10)).matchesActualCount(5));
        assertFalse(decode(VerificationModeFactory.atLeast(2)).matchesActualCount(1));
        
        assertTrue(decode(VerificationModeFactory.atLeast(10)).matchesActualCount(10));
        assertTrue(decode(VerificationModeFactory.atLeast(10)).matchesActualCount(15));
    }
    
    @Test
    public void shouldKnowIfTooLittleActualInvocations() throws Exception {
        assertTrue(decode(VerificationModeFactory.times(1)).tooLittleActualInvocations(0));
        assertTrue(decode(VerificationModeFactory.times(10)).tooLittleActualInvocations(9));
        
        assertFalse(decode(VerificationModeFactory.times(0)).tooLittleActualInvocations(0));
        assertFalse(decode(VerificationModeFactory.times(1)).tooLittleActualInvocations(1));
        assertFalse(decode(VerificationModeFactory.times(1)).tooLittleActualInvocations(2));
    }
    
    @Test
    public void shouldAtLeastModeIgnoreTooLittleActualInvocations() throws Exception {
        assertFalse(decode(VerificationModeFactory.atLeast(10)).tooLittleActualInvocations(5));        
        assertFalse(decode(VerificationModeFactory.atLeast(10)).tooLittleActualInvocations(15));        
        assertFalse(decode(VerificationModeFactory.atLeastOnce()).tooLittleActualInvocations(10));        
    }

    @Test
    public void shouldKnowIfTooLittleActualInvocationsInAtLeastMode() throws Exception {
        assertTrue(decode(VerificationModeFactory.atLeast(3)).tooLittleActualInvocationsInAtLeastMode(2));
        assertTrue(decode(VerificationModeFactory.atLeast(3)).tooLittleActualInvocationsInAtLeastMode(1));
        assertTrue(decode(VerificationModeFactory.atLeast(3)).tooLittleActualInvocationsInAtLeastMode(0));
        
        assertFalse(decode(VerificationModeFactory.atLeast(1)).tooLittleActualInvocationsInAtLeastMode(1));
        assertFalse(decode(VerificationModeFactory.atLeast(1)).tooLittleActualInvocationsInAtLeastMode(2));
    }

    @Test
    public void shouldTooLittleActualInvocationsInAtLeastModeIgnoreOtherModes() throws Exception {
        assertFalse(decode(VerificationModeFactory.times(10)).tooLittleActualInvocationsInAtLeastMode(5));        
        assertFalse(decode(VerificationModeFactory.times(10)).tooLittleActualInvocationsInAtLeastMode(15));        
    }
    
    @Test
    public void shouldKnowIfTooManyActualInvocations() throws Exception {
        assertTrue(decode(VerificationModeFactory.times(0)).tooManyActualInvocations(1));
        assertTrue(decode(VerificationModeFactory.times(10)).tooManyActualInvocations(11));
        
        assertFalse(decode(VerificationModeFactory.times(0)).tooManyActualInvocations(0));
        assertFalse(decode(VerificationModeFactory.times(1)).tooManyActualInvocations(1));
        assertFalse(decode(VerificationModeFactory.times(2)).tooManyActualInvocations(1));
    }
    
    @Test
    public void shouldKnowIfWantedCountIsZero() throws Exception {
        assertTrue(decode(VerificationModeFactory.times(0)).neverWanted());
        
        assertFalse(decode(VerificationModeFactory.times(1)).neverWanted());
        assertFalse(decode(VerificationModeFactory.times(10)).neverWanted());
        assertFalse(decode(VerificationModeFactory.atLeastOnce()).neverWanted());
    }
    
    @Test
    public void shouldKnowIfNeverWantedButInvoked() throws Exception {
        assertFalse(decode(VerificationModeFactory.times(1)).neverWantedButInvoked(0));
        assertFalse(decode(VerificationModeFactory.times(10)).neverWantedButInvoked(20));
        assertFalse(decode(VerificationModeFactory.times(0)).neverWantedButInvoked(0));
        
        assertFalse(decode(VerificationModeFactory.atLeastOnce()).neverWantedButInvoked(0));
        assertFalse(decode(VerificationModeFactory.atLeastOnce()).neverWantedButInvoked(1));
        
        assertTrue(decode(VerificationModeFactory.times(0)).neverWantedButInvoked(1));
        assertTrue(decode(VerificationModeFactory.times(0)).neverWantedButInvoked(10));
    }
}