/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification;

import java.util.List;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.mockito.internal.verification.checkers.AtLeastXNumberOfInvocationsChecker;
import org.mockito.internal.verification.checkers.AtLeastXNumberOfInvocationsInOrderChecker;
import org.mockito.internal.verification.checkers.MissingInvocationChecker;
import org.mockito.internal.verification.checkers.MissingInvocationInOrderChecker;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationMode;

public class AtLeast implements VerificationInOrderMode, VerificationMode {
    
    final int wantedCount;
    
    public AtLeast(int wantedNumberOfInvocations) {
        if (wantedNumberOfInvocations < 0) {
            throw new MockitoException("Negative value is not allowed here");
        }
        this.wantedCount = wantedNumberOfInvocations;
    }
    
    public void verify(VerificationData data) {
        MissingInvocationChecker missingInvocation = new MissingInvocationChecker();
        AtLeastXNumberOfInvocationsChecker numberOfInvocations = new AtLeastXNumberOfInvocationsChecker();
        
        if (wantedCount == 1) {
            missingInvocation.check(data.getAllInvocations(), data.getWanted());
        }
        numberOfInvocations.check(data.getAllInvocations(), data.getWanted(), wantedCount);
    }
    
    public void verifyInOrder(VerificationDataInOrder data) {
        List<Invocation> allInvocations = data.getAllInvocations();
        InvocationMatcher wanted = data.getWanted();
        
        MissingInvocationInOrderChecker missingInvocation = new MissingInvocationInOrderChecker();
        AtLeastXNumberOfInvocationsInOrderChecker numberOfCalls = new AtLeastXNumberOfInvocationsInOrderChecker(data.getOrderingContext());
        
        if (wantedCount == 1) {
            missingInvocation.check(allInvocations, wanted, this, data.getOrderingContext());
        }
        
        numberOfCalls.check(allInvocations, wanted, wantedCount);
    }

    @Override
    public String toString() {
        return "Wanted invocations count: at least " + wantedCount;
    }

    @Override
    public VerificationMode description(String description) {
        return VerificationModeFactory.description(this, description);
    }
}