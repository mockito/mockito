package org.mockito.internal.verification;

public interface DurationChecker {
    boolean isVerificationStillInProgress(long startTime);
}
