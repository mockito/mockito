/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.util.LinkedList;
import java.util.List;

import org.mockito.exceptions.base.HasStackTrace;
import org.mockito.internal.progress.VerificationModeImpl;

/**
 * Provides handful of methods to search and count invocations
 */
public class InvocationsAnalyzer {

    public Invocation findSimilarInvocation(List<Invocation> invocations, InvocationMatcher wanted, VerificationModeImpl mode) {
        List<Invocation> unverified = removeUntilLastStrictlyVerified(invocations);
        
        for (Invocation invocation : unverified) {
            String wantedMethodName = wanted.getMethod().getName();
            String currentMethodName = invocation.getMethod().getName();
            
            boolean methodNameEquals = wantedMethodName.equals(currentMethodName);
            boolean isUnverified = !invocation.isVerified();
            boolean mockIsTheSame = wanted.getInvocation().getMock() == invocation.getMock();
            
            if (methodNameEquals && isUnverified && mockIsTheSame ) {
                return invocation;
            }
        }
        
        return findFirstUnverified(unverified, wanted.getInvocation().getMock());
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
    
    public HasStackTrace findLastMatchingInvocationTrace(List<Invocation> invocations, InvocationMatcher wanted) {
        Invocation lastMatching = null;
        for (Invocation invocation : invocations) {
            if (wanted.matches(invocation)) {
                lastMatching = invocation;
            }
        }
        return lastMatching != null ? lastMatching.getStackTrace() : null;
    }

    public HasStackTrace findFirstUndesiredInvocationTrace(List<Invocation> invocations, InvocationMatcher wanted, VerificationModeImpl mode) {
        int counter = 0;
        for (Invocation invocation : invocations) {
            if (wanted.matches(invocation)) {
                counter++;
                if (counter > mode.wantedCount()) {
                    return invocation.getStackTrace();
                }
            }
        }
        throw new IllegalArgumentException("There are no undesired invocations!");
    }
    
    List<Invocation> removeUntilLastStrictlyVerified(List<Invocation> invocations) {
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