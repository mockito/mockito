/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.util.List;

import org.mockito.exceptions.base.HasStackTrace;
import org.mockito.internal.progress.VerificationMode;

/**
 * Provides handful of methods to search and count invocations
 */
public class InvocationsAnalyzer {

    //TODO add missing tests
    public int countActual(List<Invocation> invocations, InvocationMatcher wanted) {
        int actual = 0;
        for (Invocation registeredInvocation : invocations) {
            if (wanted.matches(registeredInvocation)) {
                actual++;
            }
        }

        return actual;
    }

    public Invocation findActualInvocation(List<Invocation> invocations, InvocationMatcher wanted) {
        Invocation actualbyName = null;
        for (Invocation registered : invocations) {
            String wantedMethodName = wanted.getMethod().getName();
            String registeredInvocationName = registered.getMethod().getName();
            if (wantedMethodName.equals(registeredInvocationName) && !registered.isVerified()) {
                actualbyName = registered;
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
    
    public HasStackTrace findLastInvocationStackTrace(List<Invocation> invocations, InvocationMatcher wanted) {
        Invocation lastMatching = null;
        for (Invocation registered : invocations) {
            if (wanted.matches(registered)) {
                lastMatching = registered;
            }
        }
        return lastMatching != null ? lastMatching.getStackTrace() : null;
    }

    public HasStackTrace findFirstUndesiredInvocationStackTrace(List<Invocation> invocations, InvocationMatcher wanted, VerificationMode mode) {
        int counter = 0;
        for (Invocation registered : invocations) {
            if (wanted.matches(registered)) {
                counter++;
                if (counter > mode.wantedCount()) {
                    return registered.getStackTrace();
                }
            }
        }
        throw new IllegalArgumentException("There are no undesired invocations!");
    }
}