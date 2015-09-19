package org.mockito.verification;

import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.VerificationData;

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

    public VerificationMode atMost(int maxNumberOfInvocations) {
        return copySelfWithNewVerificationMode(VerificationModeFactory.atMost(maxNumberOfInvocations));
    }

    public VerificationMode only() {
        return copySelfWithNewVerificationMode(VerificationModeFactory.only());
    }
    
}
