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

public class MockingProgressImplTest extends RequiresValidState {

    private MockingProgress mockingProgress;

    @Before
    public void setup() {
        mockingProgress = new MockingProgressImpl();
    }
    
    @Test
    public void shouldStartVerificationAndPullVerificationMode() throws Exception {
        assertNull(mockingProgress.pullVerificationMode());
        
        VerificationMode mode = VerificationMode.times(19);
        
        mockingProgress.verificationStarted(mode);
        
        assertSame(mode, mockingProgress.pullVerificationMode());
        
        assertNull(mockingProgress.pullVerificationMode());
    }
    
    @Test
    public void shouldCheckIfVerificationWasFinished() throws Exception {
        mockingProgress.verificationStarted(VerificationMode.atLeastOnce());
        try {
            mockingProgress.verificationStarted(VerificationMode.atLeastOnce());
            fail();
        } catch (MockitoException e) {}
    }
}