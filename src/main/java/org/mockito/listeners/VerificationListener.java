package org.mockito.listeners;

import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationSucceededEvent;

/**
 * This listener can be notified of verify invocations on a mock.
 *
 * For this to happen, it must be registered using {@link org.mockito.internal.progress.MockingProgress#addListener(MockitoListener)}.
 */
public interface VerificationListener extends MockitoListener {
    /**
     * Called when a verification completes successfully without throwing an exception.
     *
     * @param verificationSucceededEvent contains all information about the event.
     *                                   Consisting of the mock that was verified on,
     *                                   the {@link org.mockito.verification.VerificationMode} used
     *                                   and the {@link org.mockito.internal.verification.api.VerificationData}
     */
    void onVerificationSucceeded(VerificationSucceededEvent verificationSucceededEvent);

    void onVerificationException(Object mock, VerificationMode actualMode, Throwable failure);
}
