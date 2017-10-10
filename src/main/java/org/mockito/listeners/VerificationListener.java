/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.listeners;

import org.mockito.Incubating;
import org.mockito.verification.VerificationEvent;

/**
 * This listener can be notified of verify invocations on a mock.
 * <p>
 * For this to happen, it must be registered using {@link org.mockito.internal.progress.MockingProgress#addListener(MockitoListener)}.
 */
@Incubating
public interface VerificationListener extends MockitoListener {
    /**
     * Called after a verification happened.
     * This includes verifications that fail due to the verification mode failing.
     *
     * @param verificationEvent the event that occurred.
     */
    void onVerification(VerificationEvent verificationEvent);
}
