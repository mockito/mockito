/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.List;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

public class NoMoreInteractionsMode extends VerificationModeImpl implements VerificationMode {

    protected NoMoreInteractionsMode(int wantedNumberOfInvocations, List<? extends Object> mocksToBeVerifiedInOrder,
            Verification verification) {
        super(wantedNumberOfInvocations, mocksToBeVerifiedInOrder, verification);
    }
    
    public void verify(List<Invocation> invocations, InvocationMatcher wanted) {
        new NoMoreInvocationsVerifier().verify(invocations, wanted, null);
    }
}