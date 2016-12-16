package org.mockito;

import org.mockito.internal.MockitoCore;
import org.mockito.internal.progress.MockingProgress;

import static org.mockito.internal.exceptions.Reporter.missingMethodInvocation;
import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

public class MockitoLambda {

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

    private static <R, A extends Answer<R>> FinishableStubbing<R, A> when(R value) {
        MockingProgress mockingProgress = mockingProgress();
        mockingProgress.stubbingStarted();
        FinishableStubbing<R, A> stubbing = mockingProgress.pullFinishableStubbing();
        if (stubbing == null) {
            mockingProgress.reset();
            throw missingMethodInvocation();
        }
        return stubbing;
    }

    private static <R, A extends Answer<R>> FinishableAnswer<R, A> when() {
        MockingProgress mockingProgress = mockingProgress();
        mockingProgress.stubbingStarted();
        FinishableAnswer<R, A> stubbing = mockingProgress.finishableAnswer();
        if (stubbing == null) {
            mockingProgress.reset();
            throw missingMethodInvocation();
        }
        return stubbing;
    }

    public static class OngoingLambdaStubbingReturn3<A, B, C, R> {
        private final TriFunction<A, B, C, R> method;

        public OngoingLambdaStubbingReturn3(TriFunction<A, B, C, R> method) {
            this.method = method;
        }

        public FinishableStubbing<R, Answer3<A, B, C, R>> isInvokedWith(ArgumentMatcher<A> var1, ArgumentMatcher<B> var2, ArgumentMatcher<C> var3) {
            return when(method.apply(var1.getValue(), var2.getValue(), var3.getValue()));
        }

        public FinishableStubbing<R, Answer3<A, B, C, R>> isInvokedWith(A var1, B var2, C var3) {
            return when(method.apply(var1, var2, var3));
        }
    }

    public interface Answer<R> {}
    public interface VoidAnswer2<A, B> extends Answer<Void> {
        void answer(A argument0, B argument1) throws Throwable;
    }
    public interface Answer2<A, B, R> extends Answer<R> {
        R answer(A argument0, B argument1) throws Throwable;
    }
    public interface Answer3<A, B, C, R> extends Answer<R> {
        R answer(A argument0, B argument1, C argument2) throws Throwable;
    }

    public interface FinishableStubbing<R, A extends Answer<R>> {

        void thenReturn(R foo);

        void thenAnswer(A answer);
    }

    public static ArgumentMatcher<Integer> anyInt() {
        return () -> 0;
    }

    public static ArgumentMatcher<String> anyString() {
        return () -> "";
    }

    public interface ArgumentMatcher<T> {
        T getValue();
    }

    public static class OngoingLambdaStubbingVoid2<A, B> {
        private final DuoConsumer<A, B> method;

        public OngoingLambdaStubbingVoid2(DuoConsumer<A, B> method) {
            this.method = method;
        }

        public FinishableAnswer<Void, VoidAnswer2<A, B>> isInvokedWith(ArgumentMatcher<A> var1, ArgumentMatcher<B> var2) {
            method.apply(var1.getValue(), var2.getValue());
            return when();
        }

        public FinishableAnswer<Void, VoidAnswer2<A, B>> isInvokedWith(A var1, B var2) {
            method.apply(var1, var2);
            return when();
        }
    }

    public interface FinishableAnswer<R, A extends Answer<R>> {
        void thenAnswer(A answer);
    }
}
