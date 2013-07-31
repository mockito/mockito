/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification;

import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class VerificationAfterDelayImpl implements VerificationMode {

    private int pollingPeriod;
    private int delayMillis;
    private VerificationMode delegate;

    public VerificationAfterDelayImpl(int pollingPeriod, int delayMillis, VerificationMode delegate) {
        this.pollingPeriod = pollingPeriod;
        this.delayMillis = delayMillis;
        this.delegate = delegate;
    }
    
    public void verify(VerificationData data) {
        MockitoAssertionError error = null;
        
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime <= delayMillis) {
            try {
                delegate.verify(data);
                error = null;
            } catch (MockitoAssertionError e) {
                if (canRecoverFromFailure(delegate)) {
                    error = e;
                    sleep(pollingPeriod);
                } else {
                    throw e;
                }
            }
        }
        
        if (error != null) {
            throw error;
        }
    }

    private boolean canRecoverFromFailure(VerificationMode verificationMode) {
        return !(verificationMode instanceof AtMost || verificationMode instanceof Only || verificationMode instanceof NoMoreInteractions);
    }

    private void sleep(int sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException ie) {
            // oups. not much luck.
        }
    }

    public int getDelay() {
        return delayMillis;
    }

    public int getPollingPeriod() {
        return pollingPeriod;
    }

}
