/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.verification;

import static org.mockito.internal.exceptions.Reporter.atMostAndNeverShouldNotBeUsedWithTimeout;

import java.time.Duration;

import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.VerificationOverTimeImpl;
import org.mockito.internal.verification.VerificationWrapper;

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
     * @deprecated Use {@link Timeout#Timeout(Duration, VerificationMode)} instead.
     */
    @Deprecated
    public Timeout(long millis, VerificationMode delegate) {
        this(Duration.ofMillis(millis), delegate);
    }

    /**
     * See the javadoc for {@link VerificationWithTimeout}
     * <p>
     * Typically, you won't use this class explicitly. Instead use timeout() method on Mockito class.
     * See javadoc for {@link VerificationWithTimeout}
     */
    public Timeout(Duration timeout, VerificationMode delegate) {
        this(Duration.ofMillis(10), timeout, delegate);
    }

    /**
     * See the javadoc for {@link VerificationWithTimeout}
     */
    Timeout(Duration pollingPeriod, Duration timeout, VerificationMode delegate) {
        this(new VerificationOverTimeImpl(pollingPeriod, timeout, delegate, true));
    }

    /**
     * See the javadoc for {@link VerificationWithTimeout}
     */
    Timeout(Duration pollingPeriod, VerificationMode delegate, Timer timer) {
        this(new VerificationOverTimeImpl(pollingPeriod, delegate, true, timer));
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

}
