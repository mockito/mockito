/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

/**
 * Verifies that another verification mode (the delegate) becomes true within a certain timeframe
 * (before timeoutMillis has passed, measured from the call to verify()).
 */
public class VerificationWithTimeoutImpl implements VerificationMode {
    
    VerificationMode delegate;
    int timeoutMillis;
    int pollingPeriod;

    /**
     * Create this verification mode, to be used to verify invocation ongoing data later.
     *
     * @param pollingPeriod The frequency to poll delegate.verify(), to check whether the delegate has been satisfied
     * @param timeoutMillis The time to wait (in millis) for the delegate verification mode to be satisfied
     * @param delegate The verification mode to delegate overall success or failure to
     */
    public VerificationWithTimeoutImpl(int pollingPeriod, int timeoutMillis, VerificationMode delegate) {
        this.pollingPeriod = pollingPeriod;
        this.timeoutMillis = timeoutMillis;
        this.delegate = delegate;
    }

    /**
     * Verify the given ongoing verification data, and confirm that it satisfies the delegate verification mode
     * before the timeout has passed.
     *
     * In practice, this polls the delegate verification mode, and returns successfully as soon as
     * the delegate is satisfied. If the delegate is not satisfied before the timeout has passed, the last
     * error returned by the delegate verification mode will be thrown here in turn.
     *
     * @throws MockitoAssertionError if the delegate verification mode does not succeed before the timeout
     */
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

    private void sleep(int sleep) {
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

    public int getPollingPeriod() {
        return pollingPeriod;
    }    
}