/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.verification.Times;

import java.util.function.Function;

public class OngoingVerificationFunction<A, R> {
    private final Function<A, R> method;

    OngoingVerificationFunction(Function<A, R> method) {
        this.method = method;
    }

    public void invokedWithAnyArgs() {
        MockitoLambdaHandlerImpl.verificationMode = new Times(1);
        try {
            method.apply(null);
        } catch (NullPointerException e) {
            MockitoLambdaHandlerImpl.verificationMode = null;
            throw new AutoBoxingNullPointerException(e);
        }
    }
}
