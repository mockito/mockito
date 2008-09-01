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
    
    enum Verification { EXPLICIT, NO_MORE_WANTED, AT_LEAST };
    
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

    public boolean atLeastMode() {
        return verification == Verification.AT_LEAST;
    }

    public boolean explicitMode() {
        return verification == Verification.EXPLICIT;
    }
    
    public boolean inOrderMode() {
        return !mocksToBeVerifiedInOrder.isEmpty() && (explicitMode() || atLeastMode());
    }
    
    public boolean missingMethodMode() {
        return (explicitMode() && wantedInvocationCount > 0) || (atLeastMode() && wantedInvocationCount == 1);
    }
    
    public boolean missingMethodInOrderMode() {
        return inOrderMode() && missingMethodMode();
    }
    
    public boolean exactNumberOfInvocationsMode() {
        return !inOrderMode() && (explicitMode() || atLeastMode());
    }

    public boolean matchesActualCount(int actualCount) {
        boolean atLeast = atLeastMode() && actualCount >= wantedInvocationCount;
        boolean actualMatchesWanted = !atLeastMode() && wantedInvocationCount == actualCount;
        
        return atLeast || actualMatchesWanted;
    }
    
    public boolean tooLittleActualInvocations(int actualCount) {
        return !atLeastMode() && wantedInvocationCount > actualCount; 
    }

    public boolean tooLittleActualInvocationsInAtLeastMode(int actualCount) {
        return atLeastMode() && wantedInvocationCount > actualCount;
    }
    
    public boolean tooManyActualInvocations(int actualCount) {
        return !atLeastMode() && wantedInvocationCount < actualCount;
    }
    
    public boolean neverWanted() {
        return !atLeastMode() && wantedInvocationCount == 0;
    }
    
    public boolean neverWantedButInvoked(int actualCount) {
        return neverWanted() && actualCount > 0;
    }
    
    @Override
    public String toString() {
        return "Wanted invocations count: " + wantedInvocationCount + ", Mocks to verify in order: " + mocksToBeVerifiedInOrder;
    }

}