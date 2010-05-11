package org.mockito.verification;

import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.VerificationWithTimeoutImpl;
import org.mockito.internal.verification.api.VerificationData;

//TODO: must be called Timeout
public class VerificationWithTimeout implements Timeout {

    VerificationWithTimeoutImpl impl;

    public VerificationWithTimeout(int millis, VerificationMode delegate) {
        this(10, millis, delegate);
    }

    VerificationWithTimeout(int treshhold, int millis, VerificationMode delegate) {
        this.impl = new VerificationWithTimeoutImpl(treshhold, millis, delegate);
    }

    @Override
    public void verify(VerificationData data) {
        impl.verify(data);
    }

    @Override
    public VerificationMode atLeast(int minNumberOfInvocations) {
        return new VerificationWithTimeout(impl.getTreshhold(), impl.getTimeout(), VerificationModeFactory.atLeast(minNumberOfInvocations));
    }

    @Override
    public VerificationMode atLeastOnce() {
        return new VerificationWithTimeout(impl.getTreshhold(), impl.getTimeout(), VerificationModeFactory.atLeastOnce());
    }

    @Override
    public VerificationMode atMost(int maxNumberOfInvocations) {
        return new VerificationWithTimeout(impl.getTreshhold(), impl.getTimeout(), VerificationModeFactory.atMost(maxNumberOfInvocations));
    }

    @Override
    public VerificationMode never() {
        return new VerificationWithTimeout(impl.getTreshhold(), impl.getTimeout(), VerificationModeFactory.times(0));
    }

    @Override
    public VerificationMode only() {
        return new VerificationWithTimeout(impl.getTreshhold(), impl.getTimeout(), VerificationModeFactory.only());
    }

    @Override
    public VerificationMode times(int wantedNumberOfInvocations) {
        return new VerificationWithTimeout(impl.getTreshhold(), impl.getTimeout(), VerificationModeFactory.times(wantedNumberOfInvocations));
    }
}