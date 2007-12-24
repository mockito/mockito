/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationsFinder;

public class AllInvocationsFinder implements InvocationsFinder {
    
    public List<Invocation> getAllInvocations(List<? extends Object> mocks) {
        Set<Invocation> invocationsInOrder = new TreeSet<Invocation>(new SequenceNumberComparator());
        for (Object mock : mocks) {
            List<Invocation> fromSingleMock = MockUtil.getMockHandler(mock).getRegisteredInvocations();
            invocationsInOrder.addAll(fromSingleMock);
        }
        
        return new LinkedList<Invocation>(invocationsInOrder);
    }

    private final class SequenceNumberComparator implements Comparator<Invocation> {
        public int compare(Invocation o1, Invocation o2) {
            int comparison = o1.getSequenceNumber().compareTo(o2.getSequenceNumber());
            assert comparison != 0 : "sequence number has to be globally unique";
            return comparison;
        }
    }
}