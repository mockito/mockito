/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import java.util.Collections;
import java.util.List;

import org.mockito.exceptions.parents.MockitoException;

public class VerificationMode {
    
    enum Verification { EXPLICIT, NO_MORE_WANTED };
    
    private final Integer wantedInvocationCount;
    private final List<Object> mocksToBeVerifiedInSequence;
    private final Verification verification;
    
    private VerificationMode(Integer wantedNumberOfInvocations, List<Object> mocksToBeVerifiedInSequence, Verification verification) {
        if (wantedNumberOfInvocations != null && wantedNumberOfInvocations.intValue() < 0) {
            throw new MockitoException("Negative value is not allowed here");
        }
        this.wantedInvocationCount = wantedNumberOfInvocations;
        this.mocksToBeVerifiedInSequence = mocksToBeVerifiedInSequence;
        this.verification = verification;
    }
    
    public static VerificationMode atLeastOnce() {
        return new VerificationMode(null, Collections.emptyList(), Verification.EXPLICIT);
    }

    public static VerificationMode times(int wantedNumberOfInvocations) {
        return new VerificationMode(wantedNumberOfInvocations, Collections.emptyList(), Verification.EXPLICIT);
    }

    public static VerificationMode strict(Integer wantedNumberOfInvocations, List<Object> mocksToBeVerifiedStrictly) {
        return new VerificationMode(wantedNumberOfInvocations, mocksToBeVerifiedStrictly, Verification.EXPLICIT);
    }
    
    public static VerificationMode noMoreInteractions() {
        return new VerificationMode(null, Collections.emptyList(), Verification.NO_MORE_WANTED);
    }

    /**
     * Don't use VerificationMode class directly. 
     * <p>
     * Use Mockito.atLeastOnce() and Mockito.times()
     */
    public static void dont_use_this_class_directly_instead_use_static_methods_on_Mockito() {}
    
    public boolean atLeastOnceMode() {
        return wantedInvocationCount == null;
    }

    public Integer wantedCount() {
        return wantedInvocationCount;
    }

    public List<Object> getAllMocksToBeVerifiedInSequence() {
        return mocksToBeVerifiedInSequence;
    }

    public boolean isStrict() {
        return !mocksToBeVerifiedInSequence.isEmpty();
    }

    public boolean wantedCountIsZero() {
        return wantedInvocationCount != null && wantedInvocationCount == 0;
    }
    
    public boolean isExplicit() {
        return verification == Verification.EXPLICIT;
    }
    
    public boolean missingMethodMode() {
        return isExplicit() && (atLeastOnceMode() || wantedInvocationCount == 1);
    }

    public boolean exactNumberOfInvocationsMode() {
        return !atLeastOnceMode() && isExplicit();
    }
    
    @Override
    public String toString() {
        return "Wanted invocations count: " + wantedInvocationCount + ", Mocks to verify in order: " + mocksToBeVerifiedInSequence;
    }
}