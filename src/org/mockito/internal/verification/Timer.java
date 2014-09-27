package org.mockito.internal.verification;

public class Timer {

    private final long durationMillis;

    public Timer(long durationMillis) {
        this.durationMillis = durationMillis;
    }

    public boolean isUp(long startTime) {
        return System.currentTimeMillis() - startTime <= durationMillis;
    }
}
