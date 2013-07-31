/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class VerificationWithTimeoutImpl {
    
    VerificationMode delegate;
    int timeoutMillis;
    int pollingPeriod;

    public VerificationWithTimeoutImpl(int pollingPeriod, int timeoutMillis, VerificationMode delegate) {
        this.pollingPeriod = pollingPeriod;
        this.timeoutMillis = timeoutMillis;
        this.delegate = delegate;
    }

    public void verify(VerificationData data) {
        MockitoAssertionError error = null;
        
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime <= timeoutMillis) {
            try {
                delegate.verify(data);
                return;
            } catch (MockitoAssertionError e) {
                error = e;
                sleep(pollingPeriod);
            }
        }
        if (error != null) {
            throw error;
        }
    }

    void sleep(int sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException ie) {
            // oups. not much luck.
        }
    }
    
    public VerificationMode getDelegate() {
        return delegate;
    }

    public int getTimeout() {
        return timeoutMillis;
    }

    public int getTreshhold() {
        return pollingPeriod;
    }    
}