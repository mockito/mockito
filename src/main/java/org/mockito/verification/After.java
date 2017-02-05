/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.verification;

import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWrapper;

/**
 * See the javadoc for {@link VerificationAfterDelay}
 * <p>
 * Typically, you won't use this class explicitly. Instead use timeout() method on Mockito class.
 * See javadoc for {@link VerificationWithTimeout}
 */
public class After extends VerificationWrapper<VerificationOverTimeImpl> implements VerificationAfterDelay {

    /**
     * See the javadoc for {@link VerificationAfterDelay}
     * <p>
     * Typically, you won't use this class explicitly. Instead use timeout() method on Mockito class.
     * See javadoc for {@link VerificationWithTimeout}
     */
    public After(long delayMillis, VerificationMode verificationMode) {
        this(10, delayMillis, verificationMode);
    }

    After(long pollingPeriod, long delayMillis, VerificationMode verificationMode) {
        this(new VerificationOverTimeImpl(pollingPeriod, delayMillis, verificationMode, false));
    }

    After(VerificationOverTimeImpl verificationOverTime) {
        super(verificationOverTime);
    }

    @Override
    protected VerificationMode copySelfWithNewVerificationMode(VerificationMode verificationMode) {
        return new After(wrappedVerification.copyWithVerificationMode(verificationMode));
    }

    @Override
    public VerificationMode description(String description) {
        return VerificationModeFactory.description(this, description);
    }
}
