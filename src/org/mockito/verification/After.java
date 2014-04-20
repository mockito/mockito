package org.mockito.verification;

import org.mockito.internal.verification.VerificationOverTimeImpl;

/**
 * See the javadoc for {@link VerificationAfterDelay}
 * <p>
 * Typically, you won't use this class explicitly. Instead use timeout() method on Mockito class.
 * See javadoc for {@link VerificationWithTimeout}
 */  
public class After extends VerificationWrapper<VerificationOverTimeImpl> implements VerificationAfterDelay {
    
    /**
     * See the javadoc for {@link VerificationAfterDelay}
     * <p>
     * Typically, you won't use this class explicitly. Instead use timeout() method on Mockito class.
     * See javadoc for {@link VerificationWithTimeout}
     */
    public After(long delayMillis, VerificationMode verificationMode) {
        this(10, delayMillis, verificationMode);
    }
    
    public After(long pollingPeriod, long delayMillis, VerificationMode verificationMode) {
        super(new VerificationOverTimeImpl(pollingPeriod, delayMillis, verificationMode, false));
    }
    
    @Override
    protected VerificationMode copySelfWithNewVerificationMode(VerificationMode verificationMode) {
        return new After(wrappedVerification.getPollingPeriod(), wrappedVerification.getDuration(), verificationMode);
    }

}
