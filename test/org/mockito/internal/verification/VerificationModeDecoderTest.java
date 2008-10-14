package org.mockito.internal.verification;

import org.junit.Test;
import org.mockitoutil.TestBase;


public class VerificationModeDecoderTest extends TestBase {
    
    private VerificationModeDecoder decode(Times mode) {
        return new VerificationModeDecoder(mode);
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
    public void shouldKnowIfTooLittleActualInvocations() throws Exception {
        assertTrue(decode(VerificationModeFactory.times(1)).tooLittleActualInvocations(0));
        assertTrue(decode(VerificationModeFactory.times(10)).tooLittleActualInvocations(9));
        
        assertFalse(decode(VerificationModeFactory.times(0)).tooLittleActualInvocations(0));
        assertFalse(decode(VerificationModeFactory.times(1)).tooLittleActualInvocations(1));
        assertFalse(decode(VerificationModeFactory.times(1)).tooLittleActualInvocations(2));
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
    }
    
    @Test
    public void shouldKnowIfNeverWantedButInvoked() throws Exception {
        assertFalse(decode(VerificationModeFactory.times(1)).neverWantedButInvoked(0));
        assertFalse(decode(VerificationModeFactory.times(10)).neverWantedButInvoked(20));
        assertFalse(decode(VerificationModeFactory.times(0)).neverWantedButInvoked(0));
        
        assertTrue(decode(VerificationModeFactory.times(0)).neverWantedButInvoked(1));
        assertTrue(decode(VerificationModeFactory.times(0)).neverWantedButInvoked(10));
    }
}