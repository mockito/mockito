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

    public int countActual(List<Invocation> invocations, InvocationMatcher wanted) {
        int actual = 0;
        for (Invocation invocation : invocations) {
            if (wanted.matches(invocation)) {
                actual++;
            }
        }

        return actual;
    }

    public Invocation findActualInvocation(List<Invocation> invocations, InvocationMatcher wanted) {
        Invocation actualbyName = null;
        for (Invocation invocation : invocations) {
            String wantedMethodName = wanted.getMethod().getName();
            String currentMethodName = invocation.getMethod().getName();
            if (wantedMethodName.equals(currentMethodName) && !invocation.isVerified()) {
                actualbyName = invocation;
                break;
            }
        }
        
        return actualbyName != null ? actualbyName : findFirstUnverified(invocations);
    }
    
    public Invocation findFirstUnverified(List<Invocation> invocations) {
        for (Invocation i : invocations) {
            if (!i.isVerified()) {
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

    public List<Invocation> findFirstMatchingChunk(List<Invocation> invocations, InvocationMatcher wanted) {
        List<Invocation> chunk = new LinkedList<Invocation>();
        
        for (Invocation i : invocations) {
            if (wanted.matches(i)) {
                chunk.add(i);
            } else if (!chunk.isEmpty()) {
                break;
            }
        }
        
        return chunk;
    }
}