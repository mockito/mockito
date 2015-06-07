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
import org.mockito.internal.verification.checkers.MissingInvocationInOrderChecker;
import org.mockito.internal.verification.checkers.NonGreedyNumberOfInvocationsInOrderChecker;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationMode;

public class Calls implements VerificationMode, VerificationInOrderMode {

    final int wantedCount;

    public Calls(final int wantedNumberOfInvocations) {
        if( wantedNumberOfInvocations <= 0 ) {
            throw new MockitoException( "Negative and zero values are not allowed here" );
        }
        this.wantedCount = wantedNumberOfInvocations;
    }

    public void verify(final VerificationData data) {
        throw new MockitoException( "calls is only intended to work with InOrder" );
    }

    public void verifyInOrder(final VerificationDataInOrder data) {
        final List<Invocation> allInvocations = data.getAllInvocations();
        final InvocationMatcher wanted = data.getWanted();
        
        final MissingInvocationInOrderChecker missingInvocation = new MissingInvocationInOrderChecker();
        missingInvocation.check( allInvocations, wanted, this, data.getOrderingContext());
        final NonGreedyNumberOfInvocationsInOrderChecker numberOfCalls = new NonGreedyNumberOfInvocationsInOrderChecker();
        numberOfCalls.check( allInvocations, wanted, wantedCount, data.getOrderingContext());
    }    
    
    @Override
    public String toString() {
        return "Wanted invocations count (non-greedy): " + wantedCount;
    }

}