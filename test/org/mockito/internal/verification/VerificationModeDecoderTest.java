package org.mockito.internal.verification;

import static java.util.Arrays.*;
import static org.mockito.internal.progress.VerificationModeImpl.*;

import org.junit.Test;
import org.mockito.internal.progress.VerificationModeImpl;
import org.mockitoutil.TestBase;


public class VerificationModeDecoderTest extends TestBase {
    
    @Test
    public void shouldKnowIfIsMissingMethodInOrderMode() throws Exception {
        assertTrue(decode(inOrder(1, asList("mock"))).missingMethodInOrderMode());
        assertTrue(decode(inOrder(10, asList("mock"))).missingMethodInOrderMode());
        
        assertFalse(decode(times(10)).missingMethodInOrderMode());
        assertFalse(decode(noMoreInteractions()).missingMethodInOrderMode());
        assertFalse(decode(times(0)).missingMethodInOrderMode());
    }

    private VerificationModeDecoder decode(VerificationModeImpl mode) {
        return new VerificationModeDecoder(mode);
    }
}