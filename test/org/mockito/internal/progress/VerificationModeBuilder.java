/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import java.util.Arrays;

public class VerificationModeBuilder {

    private Integer times;

    public VerificationModeImpl strict() {
        return VerificationModeImpl.strict(times, Arrays.asList(new Object()));
    }

    public VerificationModeBuilder times(int times) {
        this.times = times;
        return this;
    }
}