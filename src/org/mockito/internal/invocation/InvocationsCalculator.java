package org.mockito.internal.invocation;

import java.util.LinkedList;
import java.util.List;

import org.mockito.exceptions.parents.HasStackTrace;
import org.mockito.internal.progress.OngoingVerifyingMode;

public class InvocationsCalculator {

    private List<Invocation> invocations = new LinkedList<Invocation>();

    public InvocationsCalculator(List<Invocation> invocations) {
        this.invocations.addAll(invocations);
    }

    public int countActual(InvocationMatcher wanted) {
        int actual = 0;
        for (Invocation registeredInvocation : invocations) {
            if (wanted.matches(registeredInvocation)) {
                actual++;
            }
        }

        return actual;
    }

    public Invocation findActualInvocation(InvocationMatcher wanted) {
        Invocation actualbyName = null;
        for (Invocation registered : invocations) {
            String wantedMethodName = wanted.getMethod().getName();
            String registeredInvocationName = registered.getMethod().getName();
            if (wantedMethodName.equals(registeredInvocationName) && !registered.isVerified()) {
                actualbyName = registered;
            }
        }
        
        return actualbyName != null ? actualbyName : getFirstUnverified();
    }
    
    public Invocation getFirstUnverified() {
        for (Invocation i : invocations) {
            if (!i.isVerified()) {
                return i;
            }
        }
        return null;
    }
    
    public HasStackTrace getLastInvocationStackTrace(InvocationMatcher wanted) {
        Invocation lastMatching = null;
        for (Invocation registered : invocations) {
            if (wanted.matches(registered)) {
                lastMatching = registered;
            }
        }
        return lastMatching != null ? lastMatching.getStackTrace() : null;
    }

    public HasStackTrace getFirstUndesiredInvocationStackTrace(InvocationMatcher wanted, OngoingVerifyingMode mode) {
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

    public List<Invocation> getInvocations() {
        return invocations;
    }
}