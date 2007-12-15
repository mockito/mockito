/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.util.List;

import org.mockito.exceptions.base.HasStackTrace;
import org.mockito.internal.progress.VerificationMode;

//TODO refactor name to something like analyzer
public class InvocationsCalculator {

    public int countActual(List<Invocation> invocations, InvocationMatcher wanted) {
        int actual = 0;
        for (Invocation registeredInvocation : invocations) {
            if (wanted.matches(registeredInvocation)) {
                actual++;
            }
        }

        return actual;
    }

    //TODO start all with find
    public Invocation findActualInvocation(List<Invocation> invocations, InvocationMatcher wanted) {
        Invocation actualbyName = null;
        for (Invocation registered : invocations) {
            String wantedMethodName = wanted.getMethod().getName();
            String registeredInvocationName = registered.getMethod().getName();
            if (wantedMethodName.equals(registeredInvocationName) && !registered.isVerified()) {
                actualbyName = registered;
            }
        }
        
        return actualbyName != null ? actualbyName : getFirstUnverified(invocations);
    }
    
    public Invocation getFirstUnverified(List<Invocation> invocations) {
        for (Invocation i : invocations) {
            if (!i.isVerified()) {
                return i;
            }
        }
        return null;
    }
    
    public HasStackTrace getLastInvocationStackTrace(List<Invocation> invocations, InvocationMatcher wanted) {
        Invocation lastMatching = null;
        for (Invocation registered : invocations) {
            if (wanted.matches(registered)) {
                lastMatching = registered;
            }
        }
        return lastMatching != null ? lastMatching.getStackTrace() : null;
    }

    public HasStackTrace getFirstUndesiredInvocationStackTrace(List<Invocation> invocations, InvocationMatcher wanted, VerificationMode mode) {
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