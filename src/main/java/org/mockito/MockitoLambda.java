package org.mockito;

import org.mockito.internal.MockitoCore;
import org.mockito.stubbing.Answer2;
import org.mockito.stubbing.Answer3;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.VoidAnswer2;

public class MockitoLambda {

    static final MockitoCore MOCKITO_CORE = new MockitoCore();

    public static <A, B, C, R> OngoingLambdaStubbingReturn3<A, B, C, R> when(TriFunction<A, B, C, R> method) {
        return new OngoingLambdaStubbingReturn3<>(method);
    }

    public static <A, B> OngoingLambdaStubbingVoid2<A, B> when(DuoConsumer<A, B> method) {
        return new OngoingLambdaStubbingVoid2<>(method);
    }

    @FunctionalInterface
    public interface TriFunction<A, B, C, R> {
        R apply(A var1, B var2, C var3);
    }

    @FunctionalInterface
    public interface DuoConsumer<A, B> {
        void apply(A var1, B var2);
    }

    public static class OngoingLambdaStubbingReturn3<A, B, C, R> {
        private final TriFunction<A, B, C, R> method;

        public OngoingLambdaStubbingReturn3(TriFunction<A, B, C, R> method) {
            this.method = method;
        }

        public FinishableStubbing3<A, B, C, R> isInvokedWith(ArgumentMatcher<A> var1, ArgumentMatcher<B> var2, ArgumentMatcher<C> var3) {
            return MOCKITO_CORE.when(method.apply(var1, var2, var3));
        }

        public FinishableStubbing3<A, B, C, R> isInvokedWith(A var1, B var2, C var3) {
            return MOCKITO_CORE.when(method.apply(var1, var2, var3));
        }
    }

    public static class FinishableStubbing3<A, B, C, R> {

        public void thenReturn(R foo) {

        }

        public void thenAnswer(Answer3<A, B, C, R> answer) {

        }
    }

    public static class OngoingLambdaStubbingVoid2<A, B> {
        private final DuoConsumer<A, B> method;

        public OngoingLambdaStubbingVoid2(DuoConsumer<A, B> method) {
            this.method = method;
        }

        public FinishableAnswer2<A, B> isInvokedWith(ArgumentMatcher<A> var1, ArgumentMatcher<B> var2) {
            return MOCKITO_CORE.when(method.apply(var1, var2););
        }

        public FinishableAnswer2<A, B> isInvokedWith(A var1, B var2) {
            return MOCKITO_CORE.when(method.apply(var1, var2));
        }
    }

    public static class FinishableAnswer2<A, B> {
        public void thenAnswer(VoidAnswer2<A, B> answer) {

        }
    }
}
