/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import java.util.Collections;
import java.util.List;

import org.mockito.exceptions.parents.MockitoException;

public class OngoingVerifyingMode {

    private final Integer wantedInvocationCount;
    private final List<Object> mocksToBeVerifiedInSequence;

    private OngoingVerifyingMode(Integer wantedNumberOfInvocations, List<Object> mocksToBeVerifiedInSequence) {
        if (wantedNumberOfInvocations != null && wantedNumberOfInvocations.intValue() < 0) {
            throw new MockitoException("Negative value is not allowed here");
        }
        this.wantedInvocationCount = wantedNumberOfInvocations;
        this.mocksToBeVerifiedInSequence = mocksToBeVerifiedInSequence;
    }
    
    public static OngoingVerifyingMode atLeastOnce() {
        return new OngoingVerifyingMode(null, Collections.emptyList());
    }

    public static OngoingVerifyingMode times(int wantedNumberOfInvocations) {
        return new OngoingVerifyingMode(wantedNumberOfInvocations, Collections.emptyList());
    }
    
    /**
     * Don't use OngoingVerifyingMode class directly. 
     * <p>
     * Use Mockito.atLeastOnce() and Mockito.times()
     */
    public static void dont_use_this_class_directly_instead_use_static_methods_on_Mockito() {}
    
    public static OngoingVerifyingMode inOrder(Integer wantedNumberOfInvocations, List<Object> mocksToBeVerifiedInOrder) {
        return new OngoingVerifyingMode(wantedNumberOfInvocations, mocksToBeVerifiedInOrder);
    }

    public boolean atLeastOnceMode() {
        return wantedInvocationCount == null;
    }

    public Integer wantedCount() {
        return wantedInvocationCount;
    }

    public List<Object> getAllMocksToBeVerifiedInSequence() {
        return mocksToBeVerifiedInSequence;
    }

    public boolean orderOfInvocationsMatters() {
        return !mocksToBeVerifiedInSequence.isEmpty();
    }

    public boolean wantedCountIsZero() {
        return wantedInvocationCount != null && wantedInvocationCount == 0;
    }
}