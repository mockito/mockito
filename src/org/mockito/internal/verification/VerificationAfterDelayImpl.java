/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification;

import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class VerificationAfterDelayImpl implements VerificationMode {

    private int delayMillis;
    private VerificationMode delegate;

    public VerificationAfterDelayImpl(int delayMillis, VerificationMode delegate) {
        this.delayMillis = delayMillis;
        this.delegate = delegate;
    }
    
    public void verify(VerificationData data) {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime <= delayMillis) {
            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException ie) {
                // Oops. Try again.
            }
        }
        
        delegate.verify(data);
    }

    public int getDelay() {
        return delayMillis;
    }

}
