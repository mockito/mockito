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

public class Times implements VerificationInOrderMode, VerificationMode {
    
    final int wantedInvocationCount;
    
    public Times(int wantedNumberOfInvocations) {
        if (wantedNumberOfInvocations < 0) {
            throw new MockitoException("Negative value is not allowed here");
        }
        this.wantedInvocationCount = wantedNumberOfInvocations;
    }
    
    public void verify(VerificationData data) {
        MissingInvocationChecker missingInvocation = new MissingInvocationChecker();
        NumberOfInvocationsChecker numberOfInvocations = new NumberOfInvocationsChecker();
        
        if (wantedInvocationCount > 0) {
            missingInvocation.verify(data.getAllInvocations(), data.getWanted());
        }
        numberOfInvocations.verify(data.getAllInvocations(), data.getWanted(), this);
    }
    
    public void verifyInOrder(VerificationData data) {
        List<Invocation> allInvocations = data.getAllInvocations();
        InvocationMatcher wanted = data.getWanted();
        
        MissingInvocationInOrderChecker missingInvocation = new MissingInvocationInOrderChecker();
        NumberOfInvocationsInOrderChecker numberOfCalls = new NumberOfInvocationsInOrderChecker();
        
        if (wantedCount() > 0) {
            missingInvocation.verify(allInvocations, wanted, this);
        }
        
        numberOfCalls.verify(allInvocations, wanted, this);
    }
    
    @Override
    public String toString() {
        return "Wanted invocations count: " + wantedCount();
    }    

    public int wantedCount() {
        return wantedInvocationCount;
    }
}