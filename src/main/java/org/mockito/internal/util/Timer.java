/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import static org.mockito.internal.exceptions.Reporter.cannotCreateTimerWithNegativeDurationTime;

import java.time.Duration;
import java.time.Instant;

public class Timer {

    private final Duration duration;
    private Instant startTime = null;

    public Timer(Duration duration) {
        validateInput(duration);
        this.duration = duration;
    }

    /**
     * Informs whether the timer is still counting down.
     */
    public boolean isCounting() {
        assert startTime != null;
        Duration elapsed = Duration.between(startTime, Instant.now());
        return elapsed.compareTo(duration) <= 0;
    }

    /**
     * Starts the timer count down.
     */
    public void start() {
        startTime = Instant.now();
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
