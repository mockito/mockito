/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import java.util.function.Supplier;

public class OngoingStubbingSupplier<R> {
    private final Supplier<R> method;

    OngoingStubbingSupplier(Supplier<R> method) {
        this.method = method;
    }

    public OngoingStubbingFunctionWithArguments invokedWithNoArgs() {
        return new OngoingStubbingFunctionWithArguments();
    }

    public class OngoingStubbingFunctionWithArguments extends ReturningStubInProgress<R> {
        public void thenAnswer(SupplierAnswer<R> answer) {
            this.setAnswerAndInvokeMethod(answer);
        }

        @Override
        public void invokeMethod() {
            method.get();
        }
    }
}
