/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.verification;

import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.VerificationWithTimeoutImpl;
import org.mockito.internal.verification.api.VerificationData;

/**
 * See the javadoc for {@link VerificationWithTimeout}
 */
public class Timeout implements VerificationWithTimeout {

    VerificationWithTimeoutImpl impl;

    /**
     * See the javadoc for {@link VerificationWithTimeout}
     */
    public Timeout(int millis, VerificationMode delegate) {
        this(10, millis, delegate);
    }

    Timeout(int treshhold, int millis, VerificationMode delegate) {
        this.impl = new VerificationWithTimeoutImpl(treshhold, millis, delegate);
    }

    public void verify(VerificationData data) {
        impl.verify(data);
    }

    public VerificationMode atLeast(int minNumberOfInvocations) {
        return new Timeout(impl.getTreshhold(), impl.getTimeout(), VerificationModeFactory.atLeast(minNumberOfInvocations));
    }

    public VerificationMode atLeastOnce() {
        return new Timeout(impl.getTreshhold(), impl.getTimeout(), VerificationModeFactory.atLeastOnce());
    }

    public VerificationMode atMost(int maxNumberOfInvocations) {
        return new Timeout(impl.getTreshhold(), impl.getTimeout(), VerificationModeFactory.atMost(maxNumberOfInvocations));
    }

    public VerificationMode never() {
        return new Timeout(impl.getTreshhold(), impl.getTimeout(), VerificationModeFactory.times(0));
    }

    public VerificationMode only() {
        return new Timeout(impl.getTreshhold(), impl.getTimeout(), VerificationModeFactory.only());
    }

    public VerificationMode times(int wantedNumberOfInvocations) {
        return new Timeout(impl.getTreshhold(), impl.getTimeout(), VerificationModeFactory.times(wantedNumberOfInvocations));
    }
}