/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.mockito.internal.util.Timer;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class InvocationSignalTest extends TestBase {

    private final IMethods mock = mock(IMethods.class);

    @Test
    public void should_not_count_invocations_registered_by_the_thread_running_the_pass() {
        InvocationSignal.Pass pass = InvocationSignal.startPass();
        mock.simpleMethod();
        pass.end();

        assertThat(pass.changedByOtherThreads()).isFalse();
    }

    @Test
    public void should_count_invocations_registered_by_other_threads_during_the_pass()
            throws Exception {
        InvocationSignal.Pass pass = InvocationSignal.startPass();
        Thread caller = new Thread(mock::simpleMethod);
        caller.start();
        caller.join();
        pass.end();

        assertThat(pass.changedByOtherThreads()).isTrue();
    }

    @Test
    public void should_not_count_invocations_registered_in_a_nested_pass_on_the_same_thread() {
        InvocationSignal.Pass outer = InvocationSignal.startPass();
        InvocationSignal.Pass inner = InvocationSignal.startPass();
        mock.simpleMethod();
        inner.end();
        outer.end();

        assertThat(outer.changedByOtherThreads()).isFalse();
    }

    @Test
    public void should_stop_waiting_when_an_invocation_is_registered_after_the_pass() {
        InvocationSignal.Pass pass = InvocationSignal.startPass();
        pass.end();

        mock.simpleMethod();

        assertStopsWaitingEarly(pass);
    }

    @Test
    public void should_wake_up_when_another_thread_registers_an_invocation_while_waiting()
            throws Exception {
        InvocationSignal.Pass pass = InvocationSignal.startPass();
        pass.end();

        Thread caller = runAfter(100, mock::simpleMethod);

        assertStopsWaitingEarly(pass);
        caller.join();
    }

    @Test
    public void should_wake_up_when_another_thread_clears_invocations_while_waiting()
            throws Exception {
        mock.simpleMethod();
        InvocationSignal.Pass pass = InvocationSignal.startPass();
        pass.end();

        Thread caller = runAfter(100, () -> clearInvocations(mock));

        assertStopsWaitingEarly(pass);
        caller.join();
    }

    @Test
    public void should_wake_up_when_another_thread_resets_the_mock_while_waiting()
            throws Exception {
        mock.simpleMethod();
        InvocationSignal.Pass pass = InvocationSignal.startPass();
        pass.end();

        Thread caller = runAfter(100, () -> reset(mock));

        assertStopsWaitingEarly(pass);
        caller.join();
    }

    private static void assertStopsWaitingEarly(InvocationSignal.Pass pass) {
        Timer timer = new Timer(TimeUnit.SECONDS.toMillis(20));
        timer.start();

        pass.awaitChange(timer);

        // Had the change been missed, the wait would have lasted until the timer ran out.
        assertThat(timer.remainingNanos()).isGreaterThan(TimeUnit.SECONDS.toNanos(10));
    }

    private static Thread runAfter(long millis, Runnable runnable) {
        Thread thread =
                new Thread(
                        () -> {
                            try {
                                Thread.sleep(millis);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            runnable.run();
                        });
        thread.start();
        return thread;
    }
}
