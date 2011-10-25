/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.verification;

import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.FriendlyReminderException;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.VerificationWithTimeoutImpl;
import org.mockito.internal.verification.api.VerificationData;

/**
 * See the javadoc for {@link VerificationWithTimeout}
 * <p>
 * Typically, you won't use this class explicitly. Instead use timeout() method on Mockito class.
 * See javadoc for {@link VerificationWithTimeout}
 */
public class Timeout implements VerificationWithTimeout {

    VerificationWithTimeoutImpl impl;

    /**
     * See the javadoc for {@link VerificationWithTimeout}
     * <p>
     * Typically, you won't use this class explicitly. Instead use timeout() method on Mockito class.
     * See javadoc for {@link VerificationWithTimeout}
     */
    public Timeout(int millis, VerificationMode delegate) {
        this(10, millis, delegate);
    }

    /**
     * See the javadoc for {@link VerificationWithTimeout}
     */
    Timeout(int treshhold, int millis, VerificationMode delegate) {
        this.impl = new VerificationWithTimeoutImpl(treshhold, millis, delegate);
    }

    /**
     * See the javadoc for {@link VerificationWithTimeout}
     */
    public void verify(VerificationData data) {
        impl.verify(data);
    }

    /**
     * See the javadoc for {@link VerificationWithTimeout}
     */
    public VerificationMode atLeast(int minNumberOfInvocations) {
        return new Timeout(impl.getTreshhold(), impl.getTimeout(), VerificationModeFactory.atLeast(minNumberOfInvocations));
    }

    /**
     * See the javadoc for {@link VerificationWithTimeout}
     */
    public VerificationMode atLeastOnce() {
        return new Timeout(impl.getTreshhold(), impl.getTimeout(), VerificationModeFactory.atLeastOnce());
    }

    /**
     * See the javadoc for {@link VerificationWithTimeout}
     */
    public VerificationMode atMost(int maxNumberOfInvocations) {
        new Reporter().atMostShouldNotBeUsedWithTimeout();
        return null;
    }

    /**
     * See the javadoc for {@link VerificationWithTimeout}
     */
    public VerificationMode never() {
        return new Timeout(impl.getTreshhold(), impl.getTimeout(), VerificationModeFactory.times(0));
    }

    /**
     * See the javadoc for {@link VerificationWithTimeout}
     */
    public VerificationMode only() {
        return new Timeout(impl.getTreshhold(), impl.getTimeout(), VerificationModeFactory.only());
    }

    /**
     * See the javadoc for {@link VerificationWithTimeout}
     */
    public VerificationMode times(int wantedNumberOfInvocations) {
        return new Timeout(impl.getTreshhold(), impl.getTimeout(), VerificationModeFactory.times(wantedNumberOfInvocations));
    }
}