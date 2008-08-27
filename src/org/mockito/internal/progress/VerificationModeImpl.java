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
    
    enum Verification { EXPLICIT, NO_MORE_WANTED };
    
    private final Integer wantedInvocationCount;
    private final Integer minInvocationCount;
    private final List<? extends Object> mocksToBeVerifiedInOrder;
    private final Verification verification;
    
    private VerificationModeImpl(Integer wantedNumberOfInvocations, Integer minNumberOfInvocations, List<? extends Object> mocksToBeVerifiedInOrder, Verification verification) {
        if (wantedNumberOfInvocations != null && wantedNumberOfInvocations < 0) {
            throw new MockitoException("Negative value is not allowed for wantedNumberOfInvocations");
        }
        if (minNumberOfInvocations != null && minNumberOfInvocations < 1) {
            throw new MockitoException("Negative value and zero are not allowed for minNumberOfInvocations");
        }
        assert mocksToBeVerifiedInOrder != null;
        this.wantedInvocationCount = wantedNumberOfInvocations;
        this.minInvocationCount = minNumberOfInvocations;
        this.mocksToBeVerifiedInOrder = mocksToBeVerifiedInOrder;
        this.verification = verification;
    }
    
    public static VerificationModeImpl atLeastOnce() {
        return atLeast(1);
    }

    public static VerificationModeImpl atLeast(int minNumberOfInvocations) {
        return new VerificationModeImpl(null, minNumberOfInvocations, Collections.emptyList(), Verification.EXPLICIT);
    }

    public static VerificationModeImpl times(int wantedNumberOfInvocations) {
        return new VerificationModeImpl(wantedNumberOfInvocations, null, Collections.emptyList(), Verification.EXPLICIT);
    }

    public static VerificationModeImpl inOrder(Integer wantedNumberOfInvocations, List<? extends Object> mocksToBeVerifiedInOrder) {
        assert !mocksToBeVerifiedInOrder.isEmpty();
        return new VerificationModeImpl(wantedNumberOfInvocations, null, mocksToBeVerifiedInOrder, Verification.EXPLICIT);
    }
    
    public static VerificationModeImpl noMoreInteractions() {
        return new VerificationModeImpl(null, null, Collections.emptyList(), Verification.NO_MORE_WANTED);
    }

    public Integer wantedCount() {
        return wantedInvocationCount;
    }

    public Integer minimumCount() {
        return minInvocationCount;
    }

    public List<? extends Object> getMocksToBeVerifiedInOrder() {
        return mocksToBeVerifiedInOrder;
    }

    public boolean atLeastMode() {
        return wantedInvocationCount == null && explicitMode();
    }

    public boolean explicitMode() {
        return verification == Verification.EXPLICIT;
    }
    
    public boolean inOrderMode() {
        return !mocksToBeVerifiedInOrder.isEmpty() && explicitMode();
    }
    
    public boolean missingMethodMode() {
        return explicitMode() && (atLeastMode() || wantedInvocationCount > 0);
    }
    
    public boolean missingMethodInOrderMode() {
        return inOrderMode() && missingMethodMode();
    }
    
    public boolean exactNumberOfInvocationsMode() {
        return !inOrderMode() && explicitMode();
    }

    public boolean matchesActualCount(int actualCount) {
        boolean atLeast = atLeastMode() && actualCount >= minInvocationCount;
        boolean actualMatchesWanted = !atLeastMode() && wantedInvocationCount == actualCount;
        
        return atLeast || actualMatchesWanted;
    }
    
    public boolean tooLittleActualInvocations(int actualCount) {
        return !atLeastMode() && wantedInvocationCount > actualCount; 
    }

    public boolean tooLittleActualInvocationsInAtLeastMode(int actualCount) {
        return atLeastMode() && minInvocationCount > actualCount;
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