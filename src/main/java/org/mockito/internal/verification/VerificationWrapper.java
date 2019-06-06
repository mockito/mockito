/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public abstract class VerificationWrapper<WrapperType extends VerificationMode> implements VerificationMode {

    protected final WrapperType wrappedVerification;

    public VerificationWrapper(WrapperType wrappedVerification) {
        this.wrappedVerification = wrappedVerification;
    }

    public void verify(VerificationData data) {
        wrappedVerification.verify(data);
    }

    protected abstract VerificationMode copySelfWithNewVerificationMode(VerificationMode verificationMode);

    public VerificationMode times(int wantedNumberOfInvocations) {
        return copySelfWithNewVerificationMode(VerificationModeFactory.times(wantedNumberOfInvocations));
    }

    public VerificationMode never() {
        return copySelfWithNewVerificationMode(VerificationModeFactory.atMost(0));
    }

    public VerificationMode atLeastOnce() {
        return copySelfWithNewVerificationMode(VerificationModeFactory.atLeastOnce());
    }

    public VerificationMode atLeast(int minNumberOfInvocations) {
        return copySelfWithNewVerificationMode(VerificationModeFactory.atLeast(minNumberOfInvocations));
    }

    public VerificationMode atMostOnce() {
        return copySelfWithNewVerificationMode(VerificationModeFactory.atMostOnce());
    }

    public VerificationMode atMost(int maxNumberOfInvocations) {
        return copySelfWithNewVerificationMode(VerificationModeFactory.atMost(maxNumberOfInvocations));
    }

    public VerificationMode only() {
        return copySelfWithNewVerificationMode(VerificationModeFactory.only());
    }

}
