/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.RequiresValidState;
import org.mockito.exceptions.parents.MockitoException;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.MockingProgressImpl;
import org.mockito.internal.progress.OngoingVerifyingMode;

public class MockingProgressImplTest extends RequiresValidState {

    private MockingProgress mockingProgress;

    @Before
    public void setup() {
        mockingProgress = new MockingProgressImpl();
    }
    
    @Test
    public void shouldSwitchVerifyingMode() throws Exception {
        assertNull(mockingProgress.pullVerifyingMode());
        
        OngoingVerifyingMode mode = OngoingVerifyingMode.times(19);
        
        mockingProgress.verifyingStarted(mode);
        
        assertSame(mode, mockingProgress.pullVerifyingMode());
    }
    
    @Test
    public void shouldCheckIfVerificationWasFinished() throws Exception {
        mockingProgress.verifyingStarted(OngoingVerifyingMode.atLeastOnce());
        try {
            mockingProgress.verifyingStarted(OngoingVerifyingMode.atLeastOnce());
            fail();
        } catch (MockitoException e) {}
    }
}