/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.progress;


import org.mockito.internal.verification.Times;
import org.mockito.internal.verification.VerificationModeFactory;

public class VerificationModeBuilder {

    private Integer times = 1;

    public Times inOrder() {
        return VerificationModeFactory.times(times);
    }

    public VerificationModeBuilder times(int times) {
        this.times = times;
        return this;
    }
}