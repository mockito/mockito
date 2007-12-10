/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static org.junit.Assert.*;

import org.junit.*;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.parents.MockitoException;

public class MockitoStateImplTest extends RequiresValidState {

    private MockitoState mockitoState;

    @Before
    public void setup() {
        mockitoState = new MockitoStateImpl();
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
        mockitoState.verifyingStarted(VerifyingMode.atLeastOnce());
        try {
            mockitoState.verifyingStarted(VerifyingMode.atLeastOnce());
            fail();
        } catch (MockitoException e) {}
    }
}