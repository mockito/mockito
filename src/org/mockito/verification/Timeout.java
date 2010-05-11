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

    @Override
    public void verify(VerificationData data) {
        impl.verify(data);
    }

    @Override
    public VerificationMode atLeast(int minNumberOfInvocations) {
        return new Timeout(impl.getTreshhold(), impl.getTimeout(), VerificationModeFactory.atLeast(minNumberOfInvocations));
    }

    @Override
    public VerificationMode atLeastOnce() {
        return new Timeout(impl.getTreshhold(), impl.getTimeout(), VerificationModeFactory.atLeastOnce());
    }

    @Override
    public VerificationMode atMost(int maxNumberOfInvocations) {
        return new Timeout(impl.getTreshhold(), impl.getTimeout(), VerificationModeFactory.atMost(maxNumberOfInvocations));
    }

    @Override
    public VerificationMode never() {
        return new Timeout(impl.getTreshhold(), impl.getTimeout(), VerificationModeFactory.times(0));
    }

    @Override
    public VerificationMode only() {
        return new Timeout(impl.getTreshhold(), impl.getTimeout(), VerificationModeFactory.only());
    }

    @Override
    public VerificationMode times(int wantedNumberOfInvocations) {
        return new Timeout(impl.getTreshhold(), impl.getTimeout(), VerificationModeFactory.times(wantedNumberOfInvocations));
    }
}