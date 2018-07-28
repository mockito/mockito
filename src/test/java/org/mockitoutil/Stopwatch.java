/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoutil;

import org.mockito.exceptions.base.MockitoAssertionError;

import java.util.concurrent.TimeUnit;

import static java.lang.String.format;
import static java.lang.System.nanoTime;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * This class can be uses as stopwatch to assert that a given time is elapsed or not.
 */
public class Stopwatch {

    /**
     * The start time in nano seconds or <code>null</code> if this stop watch was not started yet
     */
    private Long startNanos = null;

    /**
     * To create an instance use {@link #createNotStarted()}
     */
    private Stopwatch() {
    }

    /**
     * Return a new and not started {@link Stopwatch}.
     */
    public static Stopwatch createNotStarted() {
        return new Stopwatch();
    }

    public void start() {
        if (startNanos != null)
            throw new IllegalStateException("This stop watch is already started!");

        startNanos = nanoTime();
    }

    public void assertElapsedTimeIsMoreThan(long expected, TimeUnit unit) {
        long elapsedNanos = elapsedNanos();
        long expectedNanos = unit.toNanos(expected);

        if (elapsedNanos <= expectedNanos)
            fail("Expected that more than %dms elapsed! But was: %dms", expectedNanos, elapsedNanos);
    }

    public void assertElapsedTimeIsLessThan(long expected, TimeUnit unit) {
        long elapsedNanos = elapsedNanos();
        long expectedNanos = unit.toNanos(expected);

        if (elapsedNanos >= expectedNanos)
            fail("Expected that less than %dms elapsed! But was: %dms", expectedNanos, elapsedNanos);
    }

    private long elapsedNanos() {
        if (startNanos == null)
            throw new IllegalStateException("This stop watch is not started!");
        return nanoTime() - startNanos;
    }

    private static void fail(String message, long expectedNanos, long elapsedNanos) {
        throw new MockitoAssertionError(format(message, NANOSECONDS.toMillis(expectedNanos), NANOSECONDS.toMillis(elapsedNanos)));
    }

    /**
     * Waits for specific amount of millis using 'Thread.sleep()'.
     * Rethrows InterruptedException.
     */
    public void waitFor(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
