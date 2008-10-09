/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.VerificationModeImpl;
import org.mockitoutil.TestBase;

public class MockingProgressImplTest extends TestBase {

    private MockingProgress mockingProgress;

    @Before
    public void setup() {
        mockingProgress = new MockingProgressImpl();
    }
    
    @Test
    public void shouldStartVerificationAndPullVerificationMode() throws Exception {
        assertNull(mockingProgress.pullVerificationMode());
        
        VerificationMode mode = VerificationModeImpl.times(19);
        
        mockingProgress.verificationStarted(mode);
        
        assertSame(mode, mockingProgress.pullVerificationMode());
        
        assertNull(mockingProgress.pullVerificationMode());
    }
    
    @Test
    public void shouldCheckIfVerificationWasFinished() throws Exception {
        mockingProgress.verificationStarted(VerificationModeImpl.atLeastOnce());
        try {
            mockingProgress.verificationStarted(VerificationModeImpl.atLeastOnce());
            fail();
        } catch (MockitoException e) {}
    }
}