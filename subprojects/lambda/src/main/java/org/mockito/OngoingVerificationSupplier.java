/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.verification.Times;

import java.util.function.Supplier;

public class OngoingVerificationSupplier<R> {
    private final Supplier<R> method;

    OngoingVerificationSupplier(Supplier<R> method) {
        this.method = method;
    }

    public void invokedWithNoArgs() {
        MockitoLambdaHandlerImpl.verificationMode = new Times(1);
        this.method.get();
    }
}
