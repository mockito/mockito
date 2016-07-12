package org.mockito.internal.util;

import static org.mockito.internal.exceptions.Reporter.cannotCreateTimerWithNegativeDurationTime;

public class Timer {

    private final long durationMillis;
    private long startTime = -1;

    public Timer(long durationMillis) {
        validateInput(durationMillis);
        this.durationMillis = durationMillis;
    }

    /**
     * Informs whether the timer is still counting down.
     */
    public boolean isCounting() {
        assert startTime != -1;
        return System.currentTimeMillis() - startTime <= durationMillis;
    }

    /**
     * Starts the timer count down.
     */
    public void start() {
        startTime = System.currentTimeMillis();
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
