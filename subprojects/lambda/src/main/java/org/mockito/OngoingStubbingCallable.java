package org.mockito;

import org.mockito.stubbing.OngoingStubbing;

import java.util.concurrent.Callable;

public class OngoingStubbingCallable<R> {
    private final Callable<R> method;

    OngoingStubbingCallable(Callable<R> method) {
        this.method = method;
    }

    public OngoingStubbingFunctionWithArguments invokedWithNoArgs() {
        return new OngoingStubbingFunctionWithArguments();
    }

    public class OngoingStubbingFunctionWithArguments extends StubInProgress<R> {
        public void thenAnswer(CallableAnswer<R> answer) {
            MockitoLambdaHandlerImpl.answerValue = answer;

            this.invokeMethod();
        }

        @Override
        public void invokeMethod() {
            try {
                method.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
