/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.util.*;

import org.mockito.exceptions.parents.MockitoException;

public class VerifyingMode {

    private final Integer wantedInvocationCount;
    private final List<Object> mocksToBeVerifiedInSequence;

    private VerifyingMode(Integer wantedNumberOfInvocations, List<Object> mocksToBeVerifiedInSequence) {
        if (wantedNumberOfInvocations != null && wantedNumberOfInvocations.intValue() < 0) {
            throw new MockitoException("Negative value is not allowed here");
        }
        this.wantedInvocationCount = wantedNumberOfInvocations;
        this.mocksToBeVerifiedInSequence = mocksToBeVerifiedInSequence;
    }
    
    public static VerifyingMode atLeastOnce() {
        return new VerifyingMode(null, Collections.emptyList());
    }

    public static VerifyingMode times(int wantedNumberOfInvocations) {
        return new VerifyingMode(wantedNumberOfInvocations, Collections.emptyList());
    }
    
    /**
     * Don't use VerifyingMode class directly. 
     * <p>
     * Use Mockito.atLeastOnce() and Mockito.times()
     */
    public static void dont_use_this_class_directly_instead_use_static_methods_on_Mockito() {}
    
    public static VerifyingMode inOrder(Integer wantedNumberOfInvocations, List<Object> mocksToBeVerifiedInOrder) {
        return new VerifyingMode(wantedNumberOfInvocations, mocksToBeVerifiedInOrder);
    }

    boolean atLeastOnceMode() {
        return wantedInvocationCount == null;
    }

    public Integer wantedCount() {
        return wantedInvocationCount;
    }

    List<Object> getAllMocksToBeVerifiedInSequence() {
        return mocksToBeVerifiedInSequence;
    }

    boolean orderOfInvocationsMatters() {
        return !mocksToBeVerifiedInSequence.isEmpty();
    }

    public boolean wantedCountIsZero() {
        return wantedInvocationCount != null && wantedInvocationCount == 0;
    }
}