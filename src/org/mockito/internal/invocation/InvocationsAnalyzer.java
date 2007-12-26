/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.progress.VerificationModeImpl;

/**
 * Provides handful of methods to search and count invocations
 */
public class InvocationsAnalyzer {

    public Invocation findSimilarInvocation(List<Invocation> invocations, InvocationMatcher wanted, VerificationModeImpl mode) {
        for (Invocation invocation : invocations) {
            String wantedMethodName = wanted.getMethod().getName();
            String currentMethodName = invocation.getMethod().getName();
            
            boolean methodNameEquals = wantedMethodName.equals(currentMethodName);
            boolean isUnverified = !invocation.isVerified();
            boolean mockIsTheSame = wanted.getInvocation().getMock() == invocation.getMock();
            
            if (methodNameEquals && isUnverified && mockIsTheSame ) {
                return invocation;
            }
        }
        
        return findFirstUnverified(invocations, wanted.getInvocation().getMock());
    }
    
    public Invocation findFirstUnverified(List<Invocation> invocations) {
        return findFirstUnverified(invocations, null);
    }
    
    Invocation findFirstUnverified(List<Invocation> invocations, Object mock) {
        for (Invocation i : invocations) {
            boolean mockIsValid = mock == null || mock == i.getMock();
            if (!i.isVerified() && mockIsValid) {
                return i;
            }
        }
        return null;
    }
    
    public List<Invocation> removeUntilLastStrictlyVerified(List<Invocation> invocations) {
        List<Invocation> unverified = new LinkedList<Invocation>();
        for (Invocation i : invocations) {
            if (i.isVerifiedStrictly()) {
                unverified.clear();
            } else {
                unverified.add(i);
            }
        }
        return unverified;
    }
}