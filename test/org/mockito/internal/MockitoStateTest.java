package org.mockito.internal;

import static org.junit.Assert.*;

import org.junit.*;
import org.mockito.internal.*;

public class MockitoStateTest {

    private MockitoState mockitoState;

    @Before
    public void setup() {
        mockitoState = new MockitoState();
    }
    
    @Test
    public void shouldSwitchVerifyingMode() throws Exception {
        mockitoState.verifyingStarted(VerifyingMode.anyTimes());
        
        assertTrue(mockitoState.mockVerificationScenario());
        
        mockitoState.verifyingStarted(null);
        
        assertFalse(mockitoState.mockVerificationScenario());
        
        mockitoState.verifyingStarted(VerifyingMode.times(100));
        
        assertTrue(mockitoState.mockVerificationScenario());
        
        assertEquals(100, mockitoState.verifyingCompleted().getExactNumberOfInvocations());
        
        assertFalse(mockitoState.mockVerificationScenario());
    }
}