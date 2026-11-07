/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.mockitoutil.Stopwatch.createNotStarted;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.verification.opentest4j.ArgumentsAreDifferent;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;
import org.mockitousage.IMethods;
import org.mockitoutil.Stopwatch;

public class VerificationOverTimeImplTest {
    @Mock private VerificationMode delegate;
    @Mock private VerificationData data;
    private VerificationOverTimeImpl impl;

    @Before
    public void setUp() {
        openMocks(this);
        impl = new VerificationOverTimeImpl(1000, delegate, true);
    }

    @Test
    public void should_return_on_success() {
        impl.verify(data);
        verify(delegate).verify(any());
    }

    @Test
    public void should_throw_mockito_assertion_error() {
        MockitoAssertionError toBeThrown = new MockitoAssertionError("message");

        doThrow(toBeThrown).when(delegate).verify(any());
        assertThatThrownBy(
                        () -> {
                            impl.verify(data);
                        })
                .isEqualTo(toBeThrown);
    }

    @Test
    public void should_deal_with_junit_assertion_error() {
        ArgumentsAreDifferent toBeThrown = new ArgumentsAreDifferent("message", "wanted", "actual");

        doThrow(toBeThrown).when(delegate).verify(any());
        assertThatThrownBy(
                        () -> {
                            impl.verify(data);
                        })
                .isEqualTo(toBeThrown);
    }

    @Test
    public void should_bound_the_number_of_passes_during_an_after_window() throws Exception {
        AtomicInteger passes = new AtomicInteger();
        VerificationOverTimeImpl after =
                new VerificationOverTimeImpl(300, ignored -> passes.incrementAndGet(), false);
        IMethods other = mock(IMethods.class);
        Thread caller =
                new Thread(
                        () -> {
                            for (int i = 0; i < 5; i++) {
                                other.simpleMethod();
                                sleep(20);
                            }
                        });

        caller.start();
        after.verify(data);
        caller.join();

        // Passes run at the start, at the deadline, and when the invocations change in between, so
        // their number is bounded by the invocations rather than by the time. The upper bound
        // leaves
        // headroom for unrelated invocations elsewhere in the JVM. Before, after() re-ran the
        // delegate in a busy loop, millions of times in a window this long.
        assertThat(passes.get()).isBetween(2, 20);
    }

    @Test
    public void should_verify_as_usual_on_a_thread_that_is_already_interrupted() {
        AtomicInteger passes = new AtomicInteger();
        VerificationOverTimeImpl after =
                new VerificationOverTimeImpl(100, ignored -> passes.incrementAndGet(), false);

        Thread.currentThread().interrupt();
        try {
            after.verify(data);

            assertThat(passes.get()).isGreaterThanOrEqualTo(2);
            assertThat(Thread.currentThread().isInterrupted()).isTrue();
        } finally {
            Thread.interrupted();
        }
    }

    @Test
    public void should_stop_waiting_when_interrupted_while_waiting() throws Exception {
        CountDownLatch passed = new CountDownLatch(1);
        VerificationOverTimeImpl timeout =
                new VerificationOverTimeImpl(
                        20_000,
                        ignored -> {
                            passed.countDown();
                            throw new MockitoAssertionError("not yet");
                        },
                        true);
        AtomicReference<Throwable> thrown = new AtomicReference<>();
        AtomicBoolean interruptedAfterwards = new AtomicBoolean();
        Thread verifier =
                new Thread(
                        () -> {
                            try {
                                timeout.verify(data);
                            } catch (Throwable t) {
                                thrown.set(t);
                            }
                            interruptedAfterwards.set(Thread.currentThread().isInterrupted());
                        });

        verifier.start();
        passed.await();
        verifier.interrupt();
        verifier.join(TimeUnit.SECONDS.toMillis(10));

        assertThat(verifier.isAlive()).isFalse();
        assertThat(thrown.get()).hasCauseInstanceOf(InterruptedException.class);
        assertThat(interruptedAfterwards.get()).isTrue();
    }

    @Test
    public void
            should_verify_again_at_once_when_another_thread_registers_an_invocation_during_a_pass() {
        IMethods other = mock(IMethods.class);
        AtomicInteger passes = new AtomicInteger();
        VerificationOverTimeImpl timeout =
                new VerificationOverTimeImpl(
                        20_000,
                        ignored -> {
                            if (passes.incrementAndGet() == 1) {
                                // The invocation arrives after this pass has read the invocations,
                                // but before the pass ends.
                                runInAnotherThread(other::simpleMethod);
                                throw new MockitoAssertionError("not yet");
                            }
                        },
                        true);
        Stopwatch watch = createNotStarted();
        watch.start();

        timeout.verify(data);

        assertThat(passes.get()).isEqualTo(2);
        watch.assertElapsedTimeIsLessThan(10, TimeUnit.SECONDS);
    }

    @Test
    public void should_not_wrap_other_exceptions() {
        RuntimeException toBeThrown = new RuntimeException();

        doThrow(toBeThrown).when(delegate).verify(any());
        assertThatThrownBy(
                        () -> {
                            impl.verify(data);
                        })
                .isEqualTo(toBeThrown);
    }

    private static void runInAnotherThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
