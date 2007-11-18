package org.mockito.internal;

import static org.junit.Assert.*;

import org.junit.*;
import org.mockito.exceptions.UnfinishedVerificationException;

public class MockitoStateTest {

    private MockitoState mockitoState;

    @Before
    public void setup() {
        mockitoState = new MockitoState();
    }
    
    @Test
    public void shouldSwitchVerifyingMode() throws Exception {
        assertFalse(mockitoState.verificationScenario());
        
        VerifyingMode mode = VerifyingMode.times(19);
        
        mockitoState.verifyingStarted(mode);
        
        assertTrue(mockitoState.verificationScenario());
        
        assertSame(mode, mockitoState.removeVerifyingMode());
    }
    
    @Test
    public void shouldCheckIfVerificationWasFinished() throws Exception {
        mockitoState.verifyingStarted(VerifyingMode.anyTimes());
        try {
            mockitoState.verifyingStarted(VerifyingMode.anyTimes());
            fail();
        } catch (UnfinishedVerificationException e) {}
    }
}