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
    private final List<? extends Object> mocksToBeVerifiedStrictly;
    private final Verification verification;
    
    private VerificationModeImpl(Integer wantedNumberOfInvocations, List<? extends Object> mocksToBeVerifiedStrictly, Verification verification) {
        if (wantedNumberOfInvocations != null && wantedNumberOfInvocations.intValue() < 0) {
            throw new MockitoException("Negative value is not allowed here");
        }
        assert mocksToBeVerifiedStrictly != null;
        this.wantedInvocationCount = wantedNumberOfInvocations;
        this.mocksToBeVerifiedStrictly = mocksToBeVerifiedStrictly;
        this.verification = verification;
    }
    
    public static VerificationModeImpl atLeastOnce() {
        return new VerificationModeImpl(null, Collections.emptyList(), Verification.EXPLICIT);
    }

    public static VerificationModeImpl times(int wantedNumberOfInvocations) {
        return new VerificationModeImpl(wantedNumberOfInvocations, Collections.emptyList(), Verification.EXPLICIT);
    }

    public static VerificationModeImpl strict(Integer wantedNumberOfInvocations, List<? extends Object> mocksToBeVerifiedStrictly) {
        assert !mocksToBeVerifiedStrictly.isEmpty();
        return new VerificationModeImpl(wantedNumberOfInvocations, mocksToBeVerifiedStrictly, Verification.EXPLICIT);
    }
    
    public static VerificationModeImpl noMoreInteractions() {
        return new VerificationModeImpl(null, Collections.emptyList(), Verification.NO_MORE_WANTED);
    }

    public Integer wantedCount() {
        return wantedInvocationCount;
    }

    public List<? extends Object> getMocksToBeVerifiedStrictly() {
        return mocksToBeVerifiedStrictly;
    }

    boolean atLeastOnceMode() {
        return wantedInvocationCount == null && verification == Verification.EXPLICIT;
    }

    public boolean explicitMode() {
        return verification == Verification.EXPLICIT;
    }
    
    public boolean strictMode() {
        return !mocksToBeVerifiedStrictly.isEmpty() && explicitMode();
    }
    
    public boolean missingMethodMode() {
        return explicitMode() && (atLeastOnceMode() || wantedInvocationCount > 0);
    }
    
    public boolean strictlyMissingMethodMode() {
        return strictMode() && missingMethodMode();
    }
    
    public boolean exactNumberOfInvocationsMode() {
        return !strictMode() && explicitMode();
    }

    public boolean matchesActualCount(int actualCount) {
        boolean atLeastOnce = atLeastOnceMode() && actualCount > 0;
        boolean actualMatchesWanted = !atLeastOnceMode() && wantedInvocationCount == actualCount;
        
        return atLeastOnce || actualMatchesWanted;
    }
    
    public boolean tooLittleActualInvocations(int actualCount) {
        return !atLeastOnceMode() && wantedInvocationCount > actualCount;
    }
    
    public boolean tooManyActualInvocations(int actualCount) {
        return !atLeastOnceMode() && wantedInvocationCount < actualCount;
    }
    
    public boolean wantedCountIsZero() {
        return !atLeastOnceMode() && wantedInvocationCount == 0;
    }
    
    @Override
    public String toString() {
        return "Wanted invocations count: " + wantedInvocationCount + ", Mocks to verify in order: " + mocksToBeVerifiedStrictly;
    }
}