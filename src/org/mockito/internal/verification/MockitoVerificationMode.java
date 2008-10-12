/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.List;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.mockito.internal.verification.api.VerificationMode;

/**
 * Holds additional information regarding verification.
 * <p> 
 * Implements marking interface which hides details from Mockito users. 
 */
public class MockitoVerificationMode implements VerificationInOrderMode, VerificationMode {
    
    public enum Verification { EXPLICIT, NO_MORE_WANTED, AT_LEAST };
    
    private List<Object> mocksToBeVerifiedInOrder;

    final int wantedInvocationCount;
    final Verification verification;
    
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
    
    public void verify(VerificationData data) {
        if (mocksToBeVerifiedInOrder != null) {
            verifyInOrder(data);
            return;
        }
        
        MissingInvocationChecker missingInvocation = new MissingInvocationChecker();
        NumberOfInvocationsChecker numberOfInvocations = new NumberOfInvocationsChecker();
        
        //TODO duplicated
        if (wantedInvocationCount > 0 || (verification == Verification.AT_LEAST && wantedInvocationCount == 1)) {
            missingInvocation.verify(data.getAllInvocations(), data.getWanted());
        }
        numberOfInvocations.verify(data.getAllInvocations(), data.getWanted(), this);
    }
    
    public void verifyInOrder(VerificationData data) {
        List<Invocation> allInvocations = data.getAllInvocations();
        InvocationMatcher wanted = data.getWanted();
        
        MissingInvocationInOrderChecker missingInvocation = new MissingInvocationInOrderChecker();
        NumberOfInvocationsInOrderChecker numberOfCalls = new NumberOfInvocationsInOrderChecker();
        
        if (wantedCount() > 0 || (verification == Verification.AT_LEAST && wantedCount() == 1)) {
            missingInvocation.verify(allInvocations, wanted, this);
        }
        
        numberOfCalls.verify(allInvocations, wanted, this);
    }
    
    @Override
    public String toString() {
        return "Wanted invocations count: " + wantedCount() + ", Mocks to verify in order: " + mocksToBeVerifiedInOrder;
    }    

    public int wantedCount() {
        return wantedInvocationCount;
    }
    
    public Verification getVerification() {
        return verification;
    }
    
    public void setMocksToVerifiedInOrder(List<Object> mocksToBeVerifiedInOrder) {
        this.mocksToBeVerifiedInOrder = mocksToBeVerifiedInOrder;
    }
}