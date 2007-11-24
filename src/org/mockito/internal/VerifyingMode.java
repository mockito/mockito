package org.mockito.internal;

import java.util.*;

import org.mockito.exceptions.MockitoException;

public class VerifyingMode {

    private final Integer exactNumberOfInvocations;
    private final List<Object> mocksToBeVerifiedInSequence;

    private VerifyingMode(Integer exactNumberOfInvocations, List<Object> mocksToBeVerifiedInSequence) {
        if (exactNumberOfInvocations != null && exactNumberOfInvocations.intValue() < 0) {
            throw new MockitoException("Negative value is not allowed here");
        }
        this.exactNumberOfInvocations = exactNumberOfInvocations;
        this.mocksToBeVerifiedInSequence = mocksToBeVerifiedInSequence;
    }
    
    public static VerifyingMode anyTimes() {
        return new VerifyingMode(null, Collections.emptyList());
    }

    public static VerifyingMode times(int exactNumberOfInvocations) {
        return new VerifyingMode(exactNumberOfInvocations, Collections.emptyList());
    }

    public boolean numberOfInvocationsMatters() {
        return exactNumberOfInvocations != null;
    }

    public int getExactNumberOfInvocations() {
        return exactNumberOfInvocations;
    }

    public List<Object> getAllMocksToBeVerifiedInSequence() {
        return mocksToBeVerifiedInSequence;
    }

    public static VerifyingMode inSequence(int exactNumberOfInvocations, List<Object> mocks) {
        return new VerifyingMode(exactNumberOfInvocations, mocks);
    }

    public boolean orderOfInvocationsMatters() {
        return !mocksToBeVerifiedInSequence.isEmpty();
    }
}