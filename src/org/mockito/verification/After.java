package org.mockito.verification;

import org.mockito.internal.verification.VerificationAfterDelayImpl;


public class After extends VerificationWrapper<VerificationAfterDelayImpl> implements VerificationAfterDelay {
    
    /**
     * See the javadoc for {@link VerificationAfterDelay}
     * <p>
     * Typically, you won't use this class explicitly. Instead use timeout() method on Mockito class.
     * See javadoc for {@link VerificationWithTimeout}
     */    
    public After(int delayMillis, VerificationMode verificationMode) {
        super(new VerificationAfterDelayImpl(delayMillis, verificationMode));
    }
    
    @Override
    protected VerificationMode copySelfWithNewVerificationMode(VerificationMode verificationMode) {
        return new After(wrappedVerification.getDelay(), verificationMode);
    }

}
