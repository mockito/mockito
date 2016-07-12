/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.verification;

import static org.mockito.internal.exceptions.Reporter.atMostAndNeverShouldNotBeUsedWithTimeout;

import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.VerificationOverTimeImpl;

/**
 * See the javadoc for {@link VerificationWithTimeout}
 * <p>
 * Typically, you won't use this class explicitly. Instead use timeout() method on Mockito class.
 * See javadoc for {@link VerificationWithTimeout}
 */
public class Timeout extends VerificationWrapper<VerificationOverTimeImpl> implements VerificationWithTimeout {
    
    /**
     * See the javadoc for {@link VerificationWithTimeout}
     * <p>
     * Typically, you won't use this class explicitly. Instead use timeout() method on Mockito class.
     * See javadoc for {@link VerificationWithTimeout}
     */
    public Timeout(long millis, VerificationMode delegate) {
        this(10, millis, delegate);
    }

    /**
     * See the javadoc for {@link VerificationWithTimeout}
     */
    Timeout(long pollingPeriodMillis, long millis, VerificationMode delegate) {
        this(new VerificationOverTimeImpl(pollingPeriodMillis, millis, delegate, true));
    }

    /**
     * See the javadoc for {@link VerificationWithTimeout}
     */
    Timeout(long pollingPeriodMillis, VerificationMode delegate, Timer timer) {
        this(new VerificationOverTimeImpl(pollingPeriodMillis, delegate, true, timer));
    }

    Timeout(VerificationOverTimeImpl verificationOverTime) {
        super(verificationOverTime);
    }

    @Override
    protected VerificationMode copySelfWithNewVerificationMode(VerificationMode newVerificationMode) {
        return new Timeout(wrappedVerification.copyWithVerificationMode(newVerificationMode));
    }

    public VerificationMode atMost(int maxNumberOfInvocations) {
        throw atMostAndNeverShouldNotBeUsedWithTimeout();
    }

    public VerificationMode never() {
        throw atMostAndNeverShouldNotBeUsedWithTimeout();
    }

    @Override
    public VerificationMode description(String description) {
        return VerificationModeFactory.description(this, description);
    }

}
