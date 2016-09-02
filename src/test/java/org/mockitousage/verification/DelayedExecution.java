package org.mockitousage.verification;

import org.mockitousage.IMethods;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class DelayedExecution {
    private final ScheduledExecutorService executor;
    private final IMethods mock;
    private final int delay;
    private final TimeUnit timeUnit;
    private final ArrayList<Character> invocations = new ArrayList<Character>();

    DelayedExecution(ScheduledExecutorService executor, IMethods mock, int delay, TimeUnit timeUnit) {
        this.executor = executor;
        this.mock = mock;
        this.delay = delay;
        this.timeUnit = timeUnit;
    }

    void recordAsyncCall(char c) {
        invocations.add(c);
    }

    void allAsyncCallsStarted() throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(invocations.size());
        for (final Character invocation : invocations) {
            executor.execute(runnable(countDownLatch, invocation));
        }
        countDownLatch.await();
    }

    private Runnable runnable(final CountDownLatch countDownLatch, final Character invocation) {
        return new Runnable() {
            @Override
            public void run() {
                countDownLatch.countDown();
                sleep();
                mock.oneArg(invocation.charValue());
            }
        };
    }

    private void sleep() {
        try {
            timeUnit.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
