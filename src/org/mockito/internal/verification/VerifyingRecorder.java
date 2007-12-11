/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.List;

import org.mockito.exceptions.Exceptions;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.progress.OngoingVerifyingMode;

public class VerifyingRecorder<T> {

    private final RegisteredInvocations registeredInvocations;
    
    //TODO change name of invocationsFinder
    public VerifyingRecorder(InvocationsFinder invocationsFinder) {
        this.registeredInvocations = new RegisteredInvocations(invocationsFinder);
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

    public void verify(InvocationMatcher wanted, OngoingVerifyingMode mode) {
        //get unverified invocation chunk and run following verifiers:
        
        // chunks can be empty, can have invocations from different mocks or
        // have invocations only from one mock (not strict verification
        // scenario)
        
        // new MissingInvocationVerifier(), 
        // new NumberOfInvocationsVerifier()
        
        new OrderOfInvocationsVerifier().verify(registeredInvocations, wanted, mode); 
        new MissingInvocationVerifier().verify(registeredInvocations, wanted, mode); 
        new NumberOfInvocationsVerifier().verify(registeredInvocations, wanted, mode);
        
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