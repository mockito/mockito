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
    int timeout;
    int treshhold;

    public VerificationWithTimeoutImpl(int treshhold, int millis, VerificationMode delegate) {
        this.treshhold = treshhold;
        this.timeout = millis;
        this.delegate = delegate;
    }

    public void verify(VerificationData data) {
        int soFar = 0;
        MockitoAssertionError error = null;
        while (soFar <= timeout) {
            try {
                delegate.verify(data);
                return;
            } catch (MockitoAssertionError e) {
                error = e;
                soFar += treshhold;
                sleep(treshhold);
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
        return timeout;
    }

    public int getTreshhold() {
        return treshhold;
    }    
}