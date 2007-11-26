package org.mockito.internal;

import java.util.*;

public class InvocationChunk {

    private final List<Invocation> invocations = new LinkedList<Invocation>();

    public InvocationChunk(Invocation invocation) {
        invocations.add(invocation);
    }
    
    public Invocation getInvocation() {
        return invocations.get(0);
    }

    public boolean isVerified() {
        return getInvocation().isVerified();
    }

    public int getCount() {
        return invocations.size();
    }

    public void add(Invocation invocation) {
        invocations.add(invocation);
    }
    
    public String toString() {
        return getInvocation() + " x " + getCount();
    }

    public void markAllInvocationsAsVerified() {
        for (Invocation invocation : invocations) {
            invocation.markVerified();
        }
    }
}