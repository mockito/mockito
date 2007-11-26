/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.util.*;

import org.mockito.exceptions.MockitoException;

public class VerifyingMode {

    private final Integer expectedInvocationCount;
    private final List<Object> mocksToBeVerifiedInSequence;

    private VerifyingMode(Integer expectedNumberOfInvocations, List<Object> mocksToBeVerifiedInSequence) {
        if (expectedNumberOfInvocations != null && expectedNumberOfInvocations.intValue() < 0) {
            throw new MockitoException("Negative value is not allowed here");
        }
        this.expectedInvocationCount = expectedNumberOfInvocations;
        this.mocksToBeVerifiedInSequence = mocksToBeVerifiedInSequence;
    }
    
    public static VerifyingMode atLeastOnce() {
        return new VerifyingMode(null, Collections.emptyList());
    }

    public static VerifyingMode times(int expectedNumberOfInvocations) {
        return new VerifyingMode(expectedNumberOfInvocations, Collections.emptyList());
    }
    
    /**
     * Don't use VerifyingMode class directly. 
     * <p>
     * Use Mockito.atLeastOnce() and Mockito.times()
     */
    public static void dont_use_this_class_directly_instead_use_static_methods_on_Mockito() {}
    
    static VerifyingMode inOrder(Integer expectedNumberOfInvocations, List<Object> mocksToBeVerifiedInOrder) {
        return new VerifyingMode(expectedNumberOfInvocations, mocksToBeVerifiedInOrder);
    }

    boolean atLeastOnceMode() {
        return expectedInvocationCount == null;
    }

    Integer expectedCount() {
        return expectedInvocationCount;
    }

    List<Object> getAllMocksToBeVerifiedInSequence() {
        return mocksToBeVerifiedInSequence;
    }

    boolean orderOfInvocationsMatters() {
        return !mocksToBeVerifiedInSequence.isEmpty();
    }

    public boolean expectedCountIsZero() {
        return expectedInvocationCount != null && expectedInvocationCount == 0;
    }
}