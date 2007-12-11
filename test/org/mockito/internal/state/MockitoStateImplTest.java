/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.state;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
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
        
        OngoingVerifyingMode mode = OngoingVerifyingMode.times(19);
        
        mockitoState.verifyingStarted(mode);
        
        assertSame(mode, mockitoState.pullVerifyingMode());
    }
    
    @Test
    public void shouldCheckIfVerificationWasFinished() throws Exception {
        mockitoState.verifyingStarted(OngoingVerifyingMode.atLeastOnce());
        try {
            mockitoState.verifyingStarted(OngoingVerifyingMode.atLeastOnce());
            fail();
        } catch (MockitoException e) {}
    }
}