package org.mockito.listeners;

import org.mockito.verification.VerificationEvent;

/**
 * This listener can be notified of verify invocations on a mock.
 * <p>
 * For this to happen, it must be registered using {@link org.mockito.internal.progress.MockingProgress#addListener(MockitoListener)}.
 */
public interface VerificationListener extends MockitoListener {
    /**
     * @param verificationEvent the event that occurred.
     */
    void onVerification(VerificationEvent verificationEvent);
}
