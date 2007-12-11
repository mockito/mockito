/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.mockito.exceptions.parents.HasStackTrace;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationChunk;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.progress.OngoingVerifyingMode;

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
    
    public void markInvocationsAsVerified(InvocationMatcher expected, OngoingVerifyingMode mode) {
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
    
    public List<InvocationChunk> unverifiedInvocationChunks(OngoingVerifyingMode ongoingVerifyingMode) {
        Set<Invocation> allInvocationsInOrder = new TreeSet<Invocation>(
                new Comparator<Invocation>(){
                    public int compare(Invocation o1, Invocation o2) {
                        int comparison = o1.getSequenceNumber().compareTo(o2.getSequenceNumber());
                        assert comparison != 0;
                        return comparison;
                    }});
        
        List<Object> allMocksToBeVerifiedInOrder = ongoingVerifyingMode.getAllMocksToBeVerifiedInSequence();
        List<Invocation> allInvocations = invocationsFinder.allInvocationsInOrder(allMocksToBeVerifiedInOrder);
        allInvocationsInOrder.addAll(allInvocations);
        
        LinkedList<InvocationChunk> chunks = new LinkedList<InvocationChunk>();
        for (Invocation i : allInvocationsInOrder) {
            if (i.isVerifiedInOrder()) {
                continue;
            }
            if (!chunks.isEmpty() 
                    && chunks.getLast().getInvocation().equals(i)) {
                chunks.get(chunks.size()-1).add(i);
            } else {
                chunks.add(new InvocationChunk(i));
            }
        }
        
        return chunks;
    }
    
    public Invocation findActualInvocation(InvocationMatcher wanted) {
        Invocation actualbyName = null;
        for (Invocation registered : registeredInvocations) {
            String wantedMethodName = wanted.getMethod().getName();
            String registeredInvocationName = registered.getMethod().getName();
            if (wantedMethodName.equals(registeredInvocationName) && !registered.isVerified()) {
                actualbyName = registered;
            }
        }
        
        return actualbyName != null ? actualbyName : getFirstUnverified();
    }

    public int countActual(InvocationMatcher wanted) {
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

    public HasStackTrace getLastInvocationStackTrace(InvocationMatcher wanted) {
        Invocation lastMatching = null;
        for (Invocation registered : registeredInvocations) {
            if (wanted.matches(registered)) {
                lastMatching = registered;
            }
        }
        return lastMatching != null ? lastMatching.getStackTrace() : null;
    }

    public HasStackTrace getFirstUndesiredInvocationStackTrace(InvocationMatcher wanted, OngoingVerifyingMode mode) {
        int counter = 0;
        for (Invocation registered : registeredInvocations) {
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