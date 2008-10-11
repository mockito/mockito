/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.List;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.verification.VerificationMode;

/**
 * Holds additional information regarding verification.
 * <p> 
 * Implements marking interface which hides details from Mockito users. 
 */
public class MockitoVerificationMode implements VerificationMode {
    
    public enum Verification { EXPLICIT, NO_MORE_WANTED, AT_LEAST };
    
    final int wantedInvocationCount;
    final Verification verification;
    
    public MockitoVerificationMode(int wantedNumberOfInvocations, Verification verification) {
        if (verification != Verification.AT_LEAST && wantedNumberOfInvocations < 0) {
            throw new MockitoException("Negative value is not allowed here");
        }
        if (verification == Verification.AT_LEAST && wantedNumberOfInvocations < 1) {
            throw new MockitoException("Negative value or zero are not allowed here");
        }
        this.wantedInvocationCount = wantedNumberOfInvocations;
        this.verification = verification;
    }
    
    public void verify(List<Invocation> invocations, InvocationMatcher wanted) {
        MissingInvocationVerifier missingInvocation = new MissingInvocationVerifier();
        NumberOfInvocationsVerifier numberOfInvocations = new NumberOfInvocationsVerifier();
        
        //TODO duplicated
        if (wantedInvocationCount > 0 || (verification == Verification.AT_LEAST && wantedInvocationCount == 1)) {
            missingInvocation.verify(invocations, wanted, this);
        }
        numberOfInvocations.verify(invocations, wanted, this);
    }

    public int wantedCount() {
        return wantedInvocationCount;
    }
    
    public Verification getVerification() {
        return verification;
    }
    
    @Override
    public String toString() {
        return "Wanted invocations count: " + wantedInvocationCount;
    }
}