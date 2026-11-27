/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import static org.mockito.internal.exceptions.Reporter.cannotCreateTimerWithNegativeDurationTime;

import java.util.concurrent.TimeUnit;

public class Timer {

    private final long durationMillis;
    private final long durationNanos;
    private long startNanos;
    private boolean started;

    public Timer(long durationMillis) {
        validateInput(durationMillis);
        this.durationMillis = durationMillis;
        this.durationNanos = TimeUnit.MILLISECONDS.toNanos(durationMillis);
    }

    /**
     * Informs whether the timer is still counting down.
     */
    public boolean isCounting() {
        assert started;
        return elapsedNanos() <= durationNanos;
    }

    /**
     * Returns the time left before the timer stops counting down, in nanoseconds, or zero if there is
     * none left.
     */
    public long remainingNanos() {
        assert started;
        return Math.max(0, durationNanos - elapsedNanos());
    }

    /**
     * Starts the timer count down.
     */
    public void start() {
        startNanos = System.nanoTime();
        started = true;
    }

    private long elapsedNanos() {
        return System.nanoTime() - startNanos;
    }

    private void validateInput(long durationMillis) {
        if (durationMillis < 0) {
            throw cannotCreateTimerWithNegativeDurationTime(durationMillis);
        }
    }

    public long duration() {
        return durationMillis;
    }
}
