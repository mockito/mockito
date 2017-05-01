/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.MAX_PRIORITY;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.concurrent.locks.LockSupport.parkUntil;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

class DelayedExecution {
    private static final int CORE_POOL_SIZE = 3;
    /**
     * Defines the number of milliseconds we expecting a Thread might need to unpark, we use this to avoid "oversleeping" while awaiting the deadline for
     */
    private static final long MAX_EXPECTED_OVERSLEEP_MILLIS = 50;

    private final ScheduledExecutorService executor;

    public DelayedExecution() {
        this.executor = newScheduledThreadPool(CORE_POOL_SIZE, maxPrioThreadFactory());
    }

    public void callAsync(long delay, TimeUnit timeUnit, Runnable r) {
        long deadline = timeUnit.toMillis(delay) + currentTimeMillis();

        executor.submit(delayedExecution(r, deadline));
    }

    public void close() throws InterruptedException {
        executor.shutdownNow();

        if (!executor.awaitTermination(5, SECONDS)) {
            throw new IllegalStateException("This delayed execution did not terminated after 5 seconds");
        }
    }

    private static Runnable delayedExecution(final Runnable r, final long deadline) {
        return new Runnable() {
            @Override
            public void run() {
                //we park the current Thread till 50ms before we want to execute the runnable
                parkUntil(deadline - MAX_EXPECTED_OVERSLEEP_MILLIS);
                //now we closing to the deadline by burning CPU-time in a loop
                burnRemaining(deadline);

                System.out.println("[DelayedExecution] exec delay = "+(currentTimeMillis() - deadline)+"ms");

                r.run();
            }

            /**
             * Loop in tight cycles until we reach the dead line. We do this cause sleep or park is very not precise,
             * this can causes a Thread to under- or oversleep, sometimes by +50ms.
             */
            private void burnRemaining(final long deadline) {
                long remaining;
                do {
                    remaining = deadline - currentTimeMillis();
                } while (remaining > 0);
            }
        };
    }

    private static ThreadFactory maxPrioThreadFactory() {
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);  // allows the JVM to exit when clients forget to call DelayedExecution.close()
                t.setPriority(MAX_PRIORITY);
                return t;
            }
        };
    }
}
