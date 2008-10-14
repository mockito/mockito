package org.mockito.internal.verification;

import org.junit.Test;
import org.mockitoutil.TestBase;


public class VerificationModeDecoderTest extends TestBase {
    
    private VerificationModeDecoder decode(Times mode) {
        return new VerificationModeDecoder(mode);
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