/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.progress.ArgumentMatcherStorage;

import java.util.function.Function;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

public class OngoingStubbingFunction<A,R> {

    private final Function<A, R> method;
    private LambdaArgumentMatcher<A> matcher;

    public OngoingStubbingFunction(Function<A, R> method) {
        this.method = method;
    }

    public OngoingStubbingFunctionWithArguments invokedWith(LambdaArgumentMatcher<A> matcher) {
        this.matcher = matcher;
        return new OngoingStubbingFunctionWithArguments();
    }

    public OngoingStubbingFunctionWithArguments invokedWith(A value) {
        this.matcher = new Equals<>(value);
        return new OngoingStubbingFunctionWithArguments();
    }

    public OngoingStubbingFunctionWithArguments invokedWithAnyArgs() {
        this.matcher = new Any<>();
        return new OngoingStubbingFunctionWithArguments();
    }

    public class OngoingStubbingFunctionWithArguments extends StubInProgress<R> {
        public void thenAnswer(FunctionAnswer<A, R> answer) {
            this.setAnswerAndInvokeMethod(answer);
        }

        @Override
        void invokeMethod() {
            argumentMatcherStorage.reportMatcher(matcher);

            try {
                try {
                    method.apply(matcher.getValue());
                } catch (IllegalArgumentException ignored) {
                    method.apply(matcher.constructObject());
                }
            } catch (NullPointerException e) {
                this.resetState();
                throw new AutoBoxingNullPointerException(e);
            } catch (CouldNotConstructObjectException e) {
                this.resetState();
                throw e;
            }
        }
    }

}
