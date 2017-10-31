package org.mockito;

import java.util.function.Function;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

public class OngoingStubbingFunction<A,R> {

    private final Function<A, R> method;
    private LambdaArgumentMatcher<A> matcher;

    OngoingStubbingFunction(Function<A, R> method) {
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

    public class OngoingStubbingFunctionWithArguments extends StubInProgress<R> {
        public void thenAnswer(CallableFunction<A, R> answer) {
            MockitoLambdaHandlerImpl.answerValue = answer;

            this.invokeMethod();
        }

        @Override
        void invokeMethod() {
            mockingProgress().getArgumentMatcherStorage().reportMatcher(matcher);
            method.apply(matcher.getValue());
        }
    }
}
