/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.verification.Times;
import org.mockito.verification.VerificationMode;

import java.util.function.Supplier;

public class OngoingVerificationSupplier<R> {
    private final Supplier<R> method;
    private final VerificationMode mode;

    OngoingVerificationSupplier(Supplier<R> method, VerificationMode mode) {
        this.method = method;
        this.mode = mode;
    }

    public void invokedWithNoArgs() {
        MockitoLambdaHandlerImpl.verificationMode = mode;
        this.method.get();
    }
}
