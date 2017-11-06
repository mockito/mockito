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
            MockitoLambdaHandlerImpl.answerValue = answer;

            this.invokeMethod();
        }

        @Override
        void invokeMethod() {
            final ArgumentMatcherStorage argumentMatcherStorage = mockingProgress().getArgumentMatcherStorage();
            argumentMatcherStorage.reportMatcher(matcher);

            try {
                method.apply(matcher.getValue());
            } catch (NullPointerException e) {
                argumentMatcherStorage.reset();
                MockitoLambdaHandlerImpl.answerValue = null;
                throw new AutoBoxingNullPointerException(e);
            }
        }
    }

}
