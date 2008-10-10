/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.List;

import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

public class BasicVerificationMode extends VerificationModeImpl implements VerificationMode {

    public BasicVerificationMode(int wantedNumberOfInvocations, List<? extends Object> mocksToBeVerifiedInOrder,
            Verification verification) {
        super(wantedNumberOfInvocations, mocksToBeVerifiedInOrder, verification);
    }

    public void verify(List<Invocation> invocations, InvocationMatcher wanted) {
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
        
        if (wantedCount() > 0 || (verification == Verification.AT_LEAST && wantedCount() == 1)) {
            missingInvocation.verify(invocations, wanted, this);
        }

        numberOfCalls.verify(invocations, wanted, this);
    }

    private void doBasicVerification(List<Invocation> invocations, InvocationMatcher wanted) {
        MissingInvocationVerifier missingInvocation = new MissingInvocationVerifier();
        NumberOfInvocationsVerifier numberOfInvocations = new NumberOfInvocationsVerifier();
        
        //TODO duplicated
        if (wantedCount() > 0 || (verification == Verification.AT_LEAST && wantedCount() == 1)) {
            missingInvocation.verify(invocations, wanted, this);
        }
        numberOfInvocations.verify(invocations, wanted, this);
    }

    public void setMocksToBeVerifiedInOrder(List<Object> mocks) {
        this.mocksToBeVerifiedInOrder = mocks;
    }
    
    @Override
    public List<? extends Object> getMocksToBeVerifiedInOrder() {
        return mocksToBeVerifiedInOrder;
    }
}