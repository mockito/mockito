/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import java.util.Collections;
import java.util.List;

import org.mockito.exceptions.base.MockitoException;

/**
 * Holds additional information regarding verification.
 * <p> 
 * Implements marking interface which hides details from Mockito users. 
 */
public class VerificationModeImpl implements VerificationMode {
    
    public enum Verification { EXPLICIT, NO_MORE_WANTED, AT_LEAST };
    
    private final int wantedInvocationCount;
    private final List<? extends Object> mocksToBeVerifiedInOrder;
    private final Verification verification;
    
    private VerificationModeImpl(int wantedNumberOfInvocations, List<? extends Object> mocksToBeVerifiedInOrder, Verification verification) {
        if (verification != Verification.AT_LEAST && wantedNumberOfInvocations < 0) {
            throw new MockitoException("Negative value is not allowed here");
        }
        if (verification == Verification.AT_LEAST && wantedNumberOfInvocations < 1) {
            throw new MockitoException("Negative value or zero are not allowed here");
        }
        assert mocksToBeVerifiedInOrder != null;
        this.wantedInvocationCount = wantedNumberOfInvocations;
        this.mocksToBeVerifiedInOrder = mocksToBeVerifiedInOrder;
        this.verification = verification;
    }
    
    public static VerificationModeImpl atLeastOnce() {
        return atLeast(1);
    }

    public static VerificationModeImpl atLeast(int minNumberOfInvocations) {
        return new VerificationModeImpl(minNumberOfInvocations, Collections.emptyList(), Verification.AT_LEAST);
    }

    public static VerificationModeImpl times(int wantedNumberOfInvocations) {
        return new VerificationModeImpl(wantedNumberOfInvocations, Collections.emptyList(), Verification.EXPLICIT);
    }

    public static VerificationModeImpl inOrder(int wantedNumberOfInvocations, List<? extends Object> mocksToBeVerifiedInOrder) {
        assert !mocksToBeVerifiedInOrder.isEmpty();
        return new VerificationModeImpl(wantedNumberOfInvocations, mocksToBeVerifiedInOrder, Verification.EXPLICIT);
    }

    public static VerificationModeImpl inOrderAtLeast(int minNumberOfInvocations, List<? extends Object> mocksToBeVerifiedInOrder) {
        assert !mocksToBeVerifiedInOrder.isEmpty();
        return new VerificationModeImpl(minNumberOfInvocations, mocksToBeVerifiedInOrder, Verification.AT_LEAST);
    }
    
    public static VerificationModeImpl noMoreInteractions() {
        return new VerificationModeImpl(0, Collections.emptyList(), Verification.NO_MORE_WANTED);
    }

    public Integer wantedCount() {
        return wantedInvocationCount;
    }

    public List<? extends Object> getMocksToBeVerifiedInOrder() {
        return mocksToBeVerifiedInOrder;
    }
    
    public Verification getVerification() {
        return verification;
    }

    @Override
    public String toString() {
        return "Wanted invocations count: " + wantedInvocationCount + ", Mocks to verify in order: " + mocksToBeVerifiedInOrder;
    }

}