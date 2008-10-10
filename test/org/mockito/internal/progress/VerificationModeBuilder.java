/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;


import org.mockito.internal.verification.MockitoVerificationMode;
import org.mockito.internal.verification.MockitoVerificationMode.Verification;

public class VerificationModeBuilder {

    private Integer times = 1;

    public MockitoVerificationMode inOrder() {
        //TODO move to factory
        return new MockitoVerificationMode(times, Verification.EXPLICIT);
    }

    public VerificationModeBuilder times(int times) {
        this.times = times;
        return this;
    }
}