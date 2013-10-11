/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.verification;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.verification.VerificationOverTimeImpl;
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
     */
    public Timeout(int millis, VerificationMode delegate) {
        this(10, millis, delegate);
    }

    /**
     * See the javadoc for {@link VerificationWithTimeout}
     */
    Timeout(int pollingPeriodMillis, int millis, VerificationMode delegate) {
        super(new VerificationOverTimeImpl(pollingPeriodMillis, millis, delegate, true));
    }
    
    @Override
    protected VerificationMode copySelfWithNewVerificationMode(VerificationMode newVerificationMode) {
        return new Timeout(wrappedVerification.getPollingPeriod(), wrappedVerification.getDuration(), newVerificationMode);
    }
    
    public VerificationMode atMost(int maxNumberOfInvocations) {
        new Reporter().atMostAndNeverShouldNotBeUsedWithTimeout();
        return null;
    }

    public VerificationMode never() {
        new Reporter().atMostAndNeverShouldNotBeUsedWithTimeout();
        return null;
    }

}