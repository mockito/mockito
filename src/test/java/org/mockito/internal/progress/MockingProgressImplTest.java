/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.progress;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.verification.VerificationMode;
import org.mockitoutil.TestBase;

import static org.junit.Assert.*;

public class MockingProgressImplTest extends TestBase {

    private MockingProgress mockingProgress;

    @Before
    public void setup() {
        mockingProgress = new MockingProgressImpl();
    }

    @Test
    public void shouldStartVerificationAndPullVerificationMode() throws Exception {
        assertNull(mockingProgress.pullVerificationMode());

        VerificationMode mode = VerificationModeFactory.times(19);

        mockingProgress.verificationStarted(mode);

        assertSame(mode, mockingProgress.pullVerificationMode());

        assertNull(mockingProgress.pullVerificationMode());
    }

    @Test
    public void shouldCheckIfVerificationWasFinished() throws Exception {
        mockingProgress.verificationStarted(VerificationModeFactory.atLeastOnce());
        try {
            mockingProgress.verificationStarted(VerificationModeFactory.atLeastOnce());
            fail();
        } catch (MockitoException e) {}
    }

    @Test
    public void shouldNotifyListenerSafely() throws Exception {
        //when
        mockingProgress.addListener(null);

        //then no exception is thrown:
        mockingProgress.mockingStarted(null, null);
    }
}
