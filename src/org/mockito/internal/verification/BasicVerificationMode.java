/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.List;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

/**
 */
public class BasicVerificationMode extends VerificationModeImpl implements VerificationMode {

    protected BasicVerificationMode(int wantedNumberOfInvocations, List<? extends Object> mocksToBeVerifiedInOrder,
            Verification verification) {
        super(wantedNumberOfInvocations, mocksToBeVerifiedInOrder, verification);
    }

    public void verify(List<Invocation> invocations, InvocationMatcher wanted) {
        MissingInvocationVerifier missingInvocation = new MissingInvocationVerifier();
        NumberOfInvocationsVerifier numberOfInvocations = new NumberOfInvocationsVerifier();
        
        //TODO duplicated
        if (wantedCount() > 0 || (verification == Verification.AT_LEAST && wantedCount() == 1)) {
            missingInvocation.verify(invocations, wanted, this);
        }
        numberOfInvocations.verify(invocations, wanted, this);
    }
}