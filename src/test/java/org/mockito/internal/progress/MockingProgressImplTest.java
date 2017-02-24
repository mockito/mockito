/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.progress;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertSame;
import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.verification.VerificationEventImpl;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.listeners.VerificationListener;
import org.mockito.verification.VerificationEvent;
import org.mockito.verification.VerificationMode;

public class MockingProgressImplTest {

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
        } catch (MockitoException e) {
        }
    }

    @Test
    public void shouldNotifyListenerSafely() throws Exception {
        // when
        mockingProgress.addListener(null);

        // then no exception is thrown:
        mockingProgress.mockingStarted(null, null);
    }

    @Test
    public void fireVerificationEvent_noVerificationListenerRegistered_shouldThrowNoException() {
        VerificationEvent event = new VerificationEventImpl(null, null, null, null);

        mockingProgress.fireVerificationEvent(event);

    }

    @Test
    public void fireVerificationEvent_verificationListenerRegistered_shouldBeNotified() {

        AssertableVerificationListener listener = new AssertableVerificationListener();
        mockingProgress.addListener(listener);

        VerificationEvent event = new VerificationEventImpl(null, null, null, null);

        mockingProgress.fireVerificationEvent(event);

        listener.assertLastEventIsSameAs(event);

    }

    private static class AssertableVerificationListener implements VerificationListener {

        private VerificationEvent lastEvent;

        @Override
        public void onVerification(VerificationEvent verificationEvent) {
            lastEvent = verificationEvent;
        }

        public void assertLastEventIsSameAs(VerificationEvent expected) {
            assertThat(lastEvent).isSameAs(expected);
        }

    }
}
