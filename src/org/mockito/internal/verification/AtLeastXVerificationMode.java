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

public class AtLeastXVerificationMode implements VerificationInOrderMode, VerificationMode {
    
    final int wantedInvocationCount;
    
    public AtLeastXVerificationMode(int wantedNumberOfInvocations) {
        if (wantedNumberOfInvocations < 1) {
            throw new MockitoException("Negative value or zero are not allowed here");
        }
        this.wantedInvocationCount = wantedNumberOfInvocations;
    }
    
    public void verify(VerificationData data) {
        MissingInvocationChecker missingInvocation = new MissingInvocationChecker();
        NumberOfInvocationsChecker numberOfInvocations = new NumberOfInvocationsChecker();
        
        if (wantedInvocationCount == 1) {
            missingInvocation.verify(data.getAllInvocations(), data.getWanted());
        }
//        numberOfInvocations.verify(data.getAllInvocations(), data.getWanted(), this);
    }
    
    public void verifyInOrder(VerificationData data) {
        List<Invocation> allInvocations = data.getAllInvocations();
        InvocationMatcher wanted = data.getWanted();
        
        MissingInvocationInOrderChecker missingInvocation = new MissingInvocationInOrderChecker();
        NumberOfInvocationsInOrderChecker numberOfCalls = new NumberOfInvocationsInOrderChecker();
        
        if (wantedInvocationCount == 1) {
            missingInvocation.verify(allInvocations, wanted, this);
        }
        
//        numberOfCalls.verify(allInvocations, wanted, this);
    }
    
    @Override
    public String toString() {
        return "Wanted invocations count: at least " + wantedCount();
    }    

    public int wantedCount() {
        return wantedInvocationCount;
    }
}