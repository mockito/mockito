/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.mockito.internal.util.Timer;

/**
 * Signals that the invocations registered on a mock have changed, so that verification over time
 * ({@code timeout()} and {@code after()}, see {@link VerificationOverTimeImpl}) can wait for a
 * change instead of polling.
 *
 * <p>There is one signal for all mocks. A verification can read the invocations of several mocks,
 * for example in order, and {@link org.mockito.internal.verification.api.VerificationData} does
 * not give access to the invocation containers it reads. A change on an unrelated mock costs a
 * waiting verification one extra pass.
 *
 * <p>Changes are counted. A verification reads the count around each pass and waits for it to move
 * on, so a change that happens between a pass and the wait is not lost. Firing only takes the
 * monitor when a verification is actually waiting.
 *
 * <p>A pass can change the registered invocations itself: building a failure message calls
 * {@code toString()} on the arguments, which can be mocks, and argument matchers can call mocks.
 * Changes made by the verifying thread during its own pass are therefore not counted for that
 * pass, otherwise every failed pass would wake the verification up again and it would spin.
 * Changes made by other threads during a pass are counted, so an invocation that arrives while a
 * pass is running is checked straight away rather than at the deadline.
 */
public final class InvocationSignal {

    private static final AtomicLong changes = new AtomicLong();
    private static final AtomicInteger passesInProgress = new AtomicInteger();
    private static final ThreadLocal<Pass> currentPass = new ThreadLocal<>();
    private static final AtomicInteger waiting = new AtomicInteger();
    private static final Object monitor = new Object();

    private InvocationSignal() {}

    /**
     * Reports that the invocations registered on a mock have changed: one was registered or removed,
     * or the invocations were cleared or reset.
     */
    public static void fire() {
        changes.incrementAndGet();
        if (passesInProgress.get() != 0) {
            Pass pass = currentPass.get();
            if (pass != null) {
                pass.ownChanges++;
            }
        }
        if (waiting.get() != 0) {
            synchronized (monitor) {
                monitor.notifyAll();
            }
        }
    }

    /**
     * Starts a verification pass on the current thread. The caller must {@link Pass#end() end} it,
     * also when the pass throws.
     */
    static Pass startPass() {
        Pass pass = new Pass(currentPass.get(), changes.get());
        currentPass.set(pass);
        passesInProgress.incrementAndGet();
        return pass;
    }

    /** One evaluation of a verification mode, between {@link #startPass()} and {@link #end()}. */
    static final class Pass {

        private final Pass enclosing;
        private final long changesAtStart;
        private long changesAtEnd;
        private long ownChanges;

        private Pass(Pass enclosing, long changesAtStart) {
            this.enclosing = enclosing;
            this.changesAtStart = changesAtStart;
        }

        void end() {
            passesInProgress.decrementAndGet();
            if (enclosing == null) {
                currentPass.remove();
            } else {
                // A pass nested in another one, on the same thread, made its changes during the
                // enclosing pass too.
                enclosing.ownChanges += ownChanges;
                currentPass.set(enclosing);
            }
            changesAtEnd = changes.get();
        }

        /** Whether another thread changed the registered invocations while this pass ran. */
        boolean changedByOtherThreads() {
            return changesAtEnd - changesAtStart > ownChanges;
        }

        /**
         * Waits until the registered invocations change after this pass, or until the timer stops
         * counting. Returns at once if another thread already changed them while the pass ran.
         */
        void awaitChange(Timer timer) {
            if (changedByOtherThreads()) {
                return;
            }
            synchronized (monitor) {
                waiting.incrementAndGet();
                try {
                    while (changes.get() == changesAtEnd) {
                        long remainingNanos = timer.remainingNanos();
                        if (remainingNanos <= 0) {
                            return;
                        }
                        TimeUnit.NANOSECONDS.timedWait(monitor, remainingNanos);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(
                            "Thread was interrupted while waiting for an invocation", e);
                } finally {
                    waiting.decrementAndGet();
                }
            }
        }
    }
}
