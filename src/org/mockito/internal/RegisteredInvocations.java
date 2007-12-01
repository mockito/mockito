/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.util.*;

public class RegisteredInvocations {
    
    private List<Invocation> registeredInvocations = new LinkedList<Invocation>();
    private final InvocationsFinder invocationsFinder;
    
    public RegisteredInvocations(InvocationsFinder invocationsFinder) {
        this.invocationsFinder = invocationsFinder;
    }

    public void add(Invocation invocation) {
        registeredInvocations.add(invocation);        
    }

    public void removeLast() {
        registeredInvocations.remove(registeredInvocations.size()-1);
    }
    
    public void markInvocationsAsVerified(ExpectedInvocation expected, VerifyingMode mode) {
        if (mode.wantedCountIsZero()) {
            return;
        }
        
        if (mode.orderOfInvocationsMatters()) {
            List<InvocationChunk> chunks = unverifiedInvocationChunks(mode);
            chunks.get(0).markAllInvocationsAsVerified();
        } else {
            for (Invocation invocation : registeredInvocations) {
                if (expected.matches(invocation)) {
                    invocation.markVerified();
                }
            }
        }
    }
    
    public List<InvocationChunk> unverifiedInvocationChunks(VerifyingMode verifyingMode) {
        Set<Invocation> allInvocationsInOrder = new TreeSet<Invocation>(
                new Comparator<Invocation>(){
                    public int compare(Invocation o1, Invocation o2) {
                        int comparison = o1.getSequenceNumber().compareTo(o2.getSequenceNumber());
                        assert comparison != 0;
                        return comparison;
                    }});
        
        List<Object> allMocksToBeVerifiedInOrder = verifyingMode.getAllMocksToBeVerifiedInSequence();
        List<Invocation> allInvocations = invocationsFinder.allInvocationsInOrder(allMocksToBeVerifiedInOrder);
        allInvocationsInOrder.addAll(allInvocations);
        
        List<InvocationChunk> chunks = new LinkedList<InvocationChunk>();
        for (Invocation i : allInvocationsInOrder) {
            if (i.isVerifiedInOrder()) {
                continue;
            }
            if (!chunks.isEmpty() 
                    && chunks.get(chunks.size()-1).getInvocation().equals(i)) {
                chunks.get(chunks.size()-1).add(i);
            } else {
                chunks.add(new InvocationChunk(i));
            }
        }
        
        return chunks;
    }
    
    public Invocation findSimilarInvocation(ExpectedInvocation wanted) {
        for (Invocation registered : registeredInvocations) {
            String wantedMethodName = wanted.getMethod().getName();
            String registeredInvocationName = registered.getMethod().getName();
            if (wantedMethodName.equals(registeredInvocationName) && !registered.isVerified()) {
                return registered;
            }
        }

        return null;
    }
    
    public int countActual(ExpectedInvocation wanted) {
        int actual = 0;
        for (Invocation registeredInvocation : registeredInvocations) {
            if (wanted.matches(registeredInvocation)) {
                actual++;
            }
        }

        return actual;
    }

    public List<Invocation> all() {
        return registeredInvocations;
    }

    public Invocation getFirstUnverified() {
        for (Invocation registered : registeredInvocations) {
            if (!registered.isVerified()) {
                return registered;
            }
        }
        return null;
    }
}
