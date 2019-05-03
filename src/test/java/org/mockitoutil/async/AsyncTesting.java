/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoutil.async;

import java.util.LinkedList;

/**
 * Streamlines testing async code for Mockito tests.
 *
 * Instances of this class are NOT thread safe (intentionally, they are not required to be thread safe)
 *
 * //TODO convert to test rule
 */
public class AsyncTesting {

    //Sanity limit of threas. Increase it if justified.
    private final static int MAX_THREADS = 4;

    private final LinkedList<Exception> problems = new LinkedList<Exception>();
    private final LinkedList<Thread> threads = new LinkedList<Thread>();
    private boolean stopping;

    /**
     * Schedules execution of runnable with some delay.
     * Starts thread immediately and returns.
     * The thread will execute the runnable after given delay in millis.
     *
     * @param delayMillis - the delay in millis
     * @param runnable - to be executed in a thread after delay
     */
    public void runAfter(final int delayMillis, final Runnable runnable) {
        if (threads.size() == MAX_THREADS) {
            throw new RuntimeException("Please don't schedule any more threads. Figure out how to test the code with minimum amount of threads");
        }
        Thread t = new Thread() {
            public void run() {
                try {
                    Thread.sleep(delayMillis);
                    runnable.run();
                } catch (Exception e) {
                    boolean cleanStop = e instanceof InterruptedException && stopping;
                    if (!cleanStop) {
                        problems.add(e);
                    }
                }
            }
        };
        System.out.println("[AsyncTesting] Starting thread that will execute the runnable after " + delayMillis + " millis. Threads so far: " + threads.size());
        threads.add(t);
        t.start();
    }

    /**
     * Interrupts and waits for all threads to complete (using 'join()').
     * Rethrows exceptions accumulated by the execution of threads.
     */
    public void cleanUp() {
        stopping = true;
        System.out.println("[AsyncTesting] Interrupting and waiting for " + threads.size() + " threads to complete...");
        while(!threads.isEmpty()) {
            Thread t = threads.removeFirst();
            try {
                t.interrupt();
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (!problems.isEmpty()) {
            throw new RuntimeException("Caught " + problems.size() + " exception(s). First one is included as cause", problems.getFirst());
        }
    }
}
