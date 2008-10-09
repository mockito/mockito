/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

/**
 * Holds additional information regarding verification.
 * <p> 
 * Implements marking interface which hides details from Mockito users. 
 */
public class VerificationModeImpl implements VerificationMode {
    
    public enum Verification { EXPLICIT, NO_MORE_WANTED, AT_LEAST };
    
    final int wantedInvocationCount;
    final List<? extends Object> mocksToBeVerifiedInOrder;
    final Verification verification;
    
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
    
    public static VerificationMode atLeastOnce() {
        return atLeast(1);
    }

    public static VerificationMode atLeast(int minNumberOfInvocations) {
        return new VerificationModeImpl(minNumberOfInvocations, Collections.emptyList(), Verification.AT_LEAST);
    }

    public static VerificationMode times(int wantedNumberOfInvocations) {
        return new VerificationModeImpl(wantedNumberOfInvocations, Collections.emptyList(), Verification.EXPLICIT);
    }

    public static VerificationMode inOrder(int wantedNumberOfInvocations, List<? extends Object> mocksToBeVerifiedInOrder) {
        assert !mocksToBeVerifiedInOrder.isEmpty();
        return new VerificationModeImpl(wantedNumberOfInvocations, mocksToBeVerifiedInOrder, Verification.EXPLICIT);
    }

    public static VerificationMode inOrderAtLeast(int minNumberOfInvocations, List<? extends Object> mocksToBeVerifiedInOrder) {
        assert !mocksToBeVerifiedInOrder.isEmpty();
        return new VerificationModeImpl(minNumberOfInvocations, mocksToBeVerifiedInOrder, Verification.AT_LEAST);
    }
    
    public static VerificationMode noMoreInteractions() {
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

    @Override
    public void verify(List<Invocation> invocations, InvocationMatcher wanted) {
        if (new VerificationModeDecoder(this).inOrderMode()) {
            invocations = new AllInvocationsFinder().getAllInvocations(this.getMocksToBeVerifiedInOrder());
        }

        List<Verifier> verifiers = Arrays.asList(
                new MissingInvocationInOrderVerifier(),
                new NumberOfInvocationsInOrderVerifier(),
                new MissingInvocationVerifier(),
                new NumberOfInvocationsVerifier(),
                new NoMoreInvocationsVerifier());
        
        for (Verifier verifier : verifiers) {
            if (verifier.appliesTo(this)) {
                verifier.verify(invocations, wanted, this);
            }
        }
    }
}