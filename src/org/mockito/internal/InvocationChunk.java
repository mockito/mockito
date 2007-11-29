/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
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

    public int getCount() {
        return invocations.size();
    }

    public void add(Invocation invocation) {
        invocations.add(invocation);
    }
    
    public void markAllInvocationsAsVerified() {
        for (Invocation invocation : invocations) {
            invocation.markVerifiedInOrder();
            invocation.markVerified();
        }
    }
    
    public String toString() {
        return getInvocation() + " x " + getCount();
    }
}