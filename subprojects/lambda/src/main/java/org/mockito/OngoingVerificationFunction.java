/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.verification.Times;
import org.mockito.verification.VerificationMode;

import java.util.List;
import java.util.function.Function;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

public class OngoingVerificationFunction<A, R> {
    private final Function<A, R> method;
    private final VerificationMode mode;

    OngoingVerificationFunction(Function<A, R> method, VerificationMode mode) {
        this.method = method;
        this.mode = mode;
    }

    public void invokedWithAnyArgs() {
        invokeMethod();
    }

    public void invokedWith(LambdaArgumentMatcher<A> matcher) {
        mockingProgress().getArgumentMatcherStorage().reportMatcher(matcher);
        this.invokeMethod();
    }

    public void invokedWith(A object) {
        mockingProgress().getArgumentMatcherStorage().reportMatcher(new Equals<>(object));
        this.invokeMethod();
    }

    private void invokeMethod() {
        MockitoLambdaHandlerImpl.verificationMode = this.mode;
        try {
            method.apply(null);
        } catch (NullPointerException e) {
            MockitoLambdaHandlerImpl.verificationMode = null;
            throw new AutoBoxingNullPointerException(e);
        }
    }
}
