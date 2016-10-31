package org.mockito.listeners;

import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

/**
 * This listener can be notified of verify invocations on a mock.
 *
 * For this to happen, it must be registered using {@link org.mockito.internal.progress.MockingProgress#addListener(MockitoListener)}.
 */
public interface VerificationListener extends MockitoListener {
    /**
     * Called when a verification completes successfully without throwing an exception.
     *
     * @param mock the mock that verify was called with.
     * @param mode the mode that the mock will be verified with.
     * @param verificationData the data of this verification.
     */
    void onVerificationSucceeded(Object mock, VerificationMode mode, VerificationData verificationData);
}
