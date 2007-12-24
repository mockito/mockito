/*
 * Copyright (c) 2007, Szczepan Faber. 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.invocation.ActualInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.VerificationModeImpl;

class ActualInvocationsFinderStub extends ActualInvocationsFinder {
    final List<Invocation> actualToReturn = new LinkedList<Invocation>();
    List<Invocation> invocations;
    @Override public List<Invocation> findInvocations(List<Invocation> invocations, InvocationMatcher wanted,
            VerificationModeImpl mode) {
        this.invocations = invocations;
        return actualToReturn;
    }
}