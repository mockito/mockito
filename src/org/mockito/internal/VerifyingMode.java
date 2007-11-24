package org.mockito.internal;

import java.util.*;

import org.mockito.exceptions.MockitoException;

public class VerifyingMode {

    private final Integer expectedNumberOfInvocations;
    private final List<Object> mocksToBeVerifiedInSequence;

    private VerifyingMode(Integer expectedNumberOfInvocations, List<Object> mocksToBeVerifiedInSequence) {
        if (expectedNumberOfInvocations != null && expectedNumberOfInvocations.intValue() < 0) {
            throw new MockitoException("Negative value is not allowed here");
        }
        this.expectedNumberOfInvocations = expectedNumberOfInvocations;
        this.mocksToBeVerifiedInSequence = mocksToBeVerifiedInSequence;
    }
    
    public static VerifyingMode anyTimes() {
        return new VerifyingMode(null, Collections.emptyList());
    }

    public static VerifyingMode times(int expectedNumberOfInvocations) {
        return new VerifyingMode(expectedNumberOfInvocations, Collections.emptyList());
    }

    public boolean numberOfInvocationsMatters() {
        return expectedNumberOfInvocations != null;
    }

    public int getExpectedNumberOfInvocations() {
        return (expectedNumberOfInvocations==null)? 1 : expectedNumberOfInvocations;
    }

    public List<Object> getAllMocksToBeVerifiedInSequence() {
        return mocksToBeVerifiedInSequence;
    }

    public static VerifyingMode inSequence(Integer expectedNumberOfInvocations, List<Object> mocks) {
        return new VerifyingMode(expectedNumberOfInvocations, mocks);
    }

    public boolean orderOfInvocationsMatters() {
        return !mocksToBeVerifiedInSequence.isEmpty();
    }
}