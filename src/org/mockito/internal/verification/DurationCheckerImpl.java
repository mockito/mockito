package org.mockito.internal.verification;

public class DurationCheckerImpl implements DurationChecker {

    private final long durationMillis;

    public DurationCheckerImpl(long durationMillis) {
        this.durationMillis = durationMillis;
    }

    public boolean isVerificationStillInProgress(long startTime) {
        return System.currentTimeMillis() - startTime <= durationMillis;
    }
}
