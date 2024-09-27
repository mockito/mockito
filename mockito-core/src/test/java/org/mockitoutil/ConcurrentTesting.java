/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoutil;

import java.util.LinkedList;
import java.util.List;

/**
 * Utility methods for concurrent testing
 */
public class ConcurrentTesting {

    /**
     * Executes given runnable in thread and waits for completion
     */
    public static void inThread(Runnable r) throws InterruptedException {
        Thread t = new Thread(r);
        t.start();
        t.join();
    }

    /**
     * Starts all supplied runnables and then waits for all of them to complete.
     * Runnables are executed concurrently.
     */
    public static void concurrently(Runnable... runnables) throws InterruptedException {
        List<Thread> threads = new LinkedList<Thread>();
        for (Runnable r : runnables) {
            Thread t = new Thread(r);
            t.start();
            threads.add(t);
        }

        for (Thread t : threads) {
            t.join();
        }
    }
}
