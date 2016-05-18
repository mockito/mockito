package org.mockito.verification;

import org.mockito.internal.verification.VerificationAfterDelayImpl;
import org.mockito.internal.verification.VerificationModeFactory;

/**
 * See the javadoc for {@link VerificationAfterDelay}
 * <p>
 * Typically, you won't use this class explicitly. Instead use timeout() method on Mockito class.
 */  
public class After extends VerificationWrapper<VerificationAfterDelayImpl> implements VerificationAfterDelay {
    
    public After(long pollingPeriod, VerificationMode verificationMode) {
        this(new VerificationAfterDelayImpl(pollingPeriod, verificationMode));
    }

    private After(VerificationAfterDelayImpl verificationOverTime) {
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