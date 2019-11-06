/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import static org.mockito.internal.exceptions.Reporter.cannotCreateTimerWithNegativeDurationTime;

import java.time.Duration;

public class Timer {

    private final Duration duration;
    private long startTime = -1;

    public Timer(Duration duration) {
        validateInput(duration);
        this.duration = duration;
    }

    /**
     * Informs whether the timer is still counting down.
     */
    public boolean isCounting() {
        assert startTime != -1;
        return System.currentTimeMillis() - startTime <= duration.toMillis();
    }

    /**
     * Starts the timer count down.
     */
    public void start() {
        startTime = System.currentTimeMillis();
    }

    private void validateInput(Duration duration) {
        if (duration.isNegative()) {
            throw cannotCreateTimerWithNegativeDurationTime(duration);
        }
    }

    public Duration duration() {
        return duration;
    }
}
