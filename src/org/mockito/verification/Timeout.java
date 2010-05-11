package org.mockito.verification;

import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.verification.api.VerificationData;

public class Timeout implements FluentVerificationMode {

    private final VerificationMode delegate;
    private final int max;
    private final int treshhold;

    public Timeout(int millis, VerificationMode delegate) {
        this(10, millis, delegate);
    }

    Timeout(int treshhold, int millis, VerificationMode delegate) {
        this.treshhold = treshhold;
        this.max = millis;
        this.delegate = delegate;
    }

    @Override
    public void verify(VerificationData data) {
        int soFar = 0;
        MockitoAssertionError error = null;
        while (soFar <= max) {
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

    private void sleep(int sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException ie) {
            // TODO
            throw new MockitoException("TODO");
        }
    }
}