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

public class AtLeast implements VerificationInOrderMode, VerificationMode {
    
    final int wantedCount;
    
    public AtLeast(int wantedNumberOfInvocations) {
        if (wantedNumberOfInvocations <= 0) {
            throw new MockitoException("Negative value or zero are not allowed here");
        }
        this.wantedCount = wantedNumberOfInvocations;
    }
    
    public void verify(VerificationData data) {
        MissingInvocationChecker missingInvocation = new MissingInvocationChecker();
        AtLeastXNumberOfInvocationsChecker numberOfInvocations = new AtLeastXNumberOfInvocationsChecker();
        
        if (wantedCount == 1) {
            missingInvocation.verify(data.getAllInvocations(), data.getWanted());
        }
        numberOfInvocations.verify(data.getAllInvocations(), data.getWanted(), wantedCount);
    }
    
    public void verifyInOrder(VerificationData data) {
        List<Invocation> allInvocations = data.getAllInvocations();
        InvocationMatcher wanted = data.getWanted();
        
        MissingInvocationInOrderChecker missingInvocation = new MissingInvocationInOrderChecker();
        AtLeastXNumberOfInvocationsInOrderChecker numberOfCalls = new AtLeastXNumberOfInvocationsInOrderChecker();
        
        if (wantedCount == 1) {
            missingInvocation.verify(allInvocations, wanted, this);
        }
        
        numberOfCalls.verify(allInvocations, wanted, wantedCount);
    }
    
    @Override
    public String toString() {
        return "Wanted invocations count: at least " + wantedCount;
    }
}