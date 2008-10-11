/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.LinkedList;
import java.util.List;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.verification.VerificationMode;

/**
 * Holds additional information regarding verification.
 * <p> 
 * Implements marking interface which hides details from Mockito users. 
 */
public class MockitoVerificationMode implements VerificationMode {
    
    public enum Verification { EXPLICIT, NO_MORE_WANTED, AT_LEAST };
    
    final int wantedInvocationCount;
    final Verification verification;
    
    List<? extends Object> mocksToBeVerifiedInOrder = new LinkedList<Object>();
    
    public MockitoVerificationMode(int wantedNumberOfInvocations, Verification verification) {
        if (verification != Verification.AT_LEAST && wantedNumberOfInvocations < 0) {
            throw new MockitoException("Negative value is not allowed here");
        }
        if (verification == Verification.AT_LEAST && wantedNumberOfInvocations < 1) {
            throw new MockitoException("Negative value or zero are not allowed here");
        }
        this.wantedInvocationCount = wantedNumberOfInvocations;
        this.verification = verification;
    }
    
    public void verify(List<Invocation> invocations, InvocationMatcher wanted) {
        //TODO null-check or isEmpty?
        if (mocksToBeVerifiedInOrder.isEmpty()) {
            doBasicVerification(invocations, wanted);
        } else {            
            doInOrderVerification(wanted);
        }
    }

    private void doInOrderVerification(InvocationMatcher wanted) {
        List<Invocation> invocations;
        invocations = new AllInvocationsFinder().getAllInvocations(mocksToBeVerifiedInOrder);

        MissingInvocationInOrderVerifier missingInvocation = new MissingInvocationInOrderVerifier();
        NumberOfInvocationsInOrderVerifier numberOfCalls = new NumberOfInvocationsInOrderVerifier();
        
        if (wantedInvocationCount > 0 || (verification == Verification.AT_LEAST && wantedInvocationCount == 1)) {
            missingInvocation.verify(invocations, wanted, this);
        }

        numberOfCalls.verify(invocations, wanted, this);
    }

    private void doBasicVerification(List<Invocation> invocations, InvocationMatcher wanted) {
        MissingInvocationVerifier missingInvocation = new MissingInvocationVerifier();
        NumberOfInvocationsVerifier numberOfInvocations = new NumberOfInvocationsVerifier();
        
        //TODO duplicated
        if (wantedInvocationCount > 0 || (verification == Verification.AT_LEAST && wantedInvocationCount == 1)) {
            missingInvocation.verify(invocations, wanted, this);
        }
        numberOfInvocations.verify(invocations, wanted, this);
    }

    public void setMocksToBeVerifiedInOrder(List<Object> mocks) {
        this.mocksToBeVerifiedInOrder = mocks;
    }
    public int wantedCount() {
        return wantedInvocationCount;
    }
    
    public Verification getVerification() {
        return verification;
    }
    
    @Override
    public String toString() {
        return "Wanted invocations count: " + wantedInvocationCount + ", Mocks to verify in order: " + mocksToBeVerifiedInOrder;
    }
}