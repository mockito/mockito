/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.util.List;

import org.mockito.exceptions.Exceptions;

public class VerifyingRecorder<T> {

    private RegisteredInvocations registeredInvocations = new RegisteredInvocations(new AllInvocationsFinder());
    private final Verifier[] verifiers;

    public VerifyingRecorder(Verifier ... verifiers) {
        this.verifiers = verifiers;
    }
    
    public void recordInvocation(InvocationMatcher invocation) {
        this.registeredInvocations.add(invocation.getInvocation());
    }
    
    public void eraseLastInvocation() {
        registeredInvocations.removeLast();
    }

    public List<Invocation> getRegisteredInvocations() {
        return registeredInvocations.all();
    }

    public void verify(InvocationMatcher wanted, VerifyingMode mode) {
        for (Verifier v : verifiers) {
            v.verify(registeredInvocations, wanted, mode);
        }
        registeredInvocations.markInvocationsAsVerified(wanted, mode);
    }
    
    public void verifyNoMoreInteractions() {
        Invocation unverified = registeredInvocations.getFirstUnverified();
        if (unverified != null) {
            Exceptions.noMoreInteractionsWanted(unverified.toString(), unverified.getStackTrace());
        }
    }
    
    public void verifyZeroInteractions() {
        Invocation unverified = registeredInvocations.getFirstUnverified();
        if (unverified != null) {
            Exceptions.zeroInteractionsWanted(unverified.toString(), unverified.getStackTrace());
        }
    }
}