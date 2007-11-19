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
        assertNull(mockitoState.pullVerifyingMode());
        
        VerifyingMode mode = VerifyingMode.times(19);
        
        mockitoState.verifyingStarted(mode);
        
        assertSame(mode, mockitoState.pullVerifyingMode());
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