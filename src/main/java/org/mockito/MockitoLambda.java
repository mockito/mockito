package org.mockito;

import org.mockito.internal.MockitoCore;
import org.mockito.stubbing.OngoingStubbing;

public class MockitoLambda {

    static final MockitoCore MOCKITO_CORE = new MockitoCore();

    public static <A, B, C, R> OngoingLambdaStubbing<A, B, C, R> when(TriFunction<A, B, C, R> method) {
        return new OngoingLambdaStubbing<>(method);
    }

    @FunctionalInterface
    public interface TriFunction<A, B, C, R> {
        R apply(A var1, B var2, C var3);
    }

    public static class OngoingLambdaStubbing<A, B, C, R> {
        private final TriFunction<A, B, C, R> method;

        public OngoingLambdaStubbing(TriFunction<A, B, C, R> method) {
            this.method = method;
        }

        public OngoingStubbing<R> isInvokedWith(ArgumentMatcher<A> var1, ArgumentMatcher<B> var2, ArgumentMatcher<C> var3) {
            return MOCKITO_CORE.when(method.apply(var1, var2, var3));
        }
    }
}
