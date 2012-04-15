/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.invocation.finder;

import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.Invocation;

import java.util.*;

public class AllInvocationsFinder {
    
    /**
     * gets all invocations from mocks. Invocations are ordered earlier first. 
     * 
     * @param mocks mocks
     * @return invocations
     */
    public List<Invocation> find(List<?> mocks) {
        Set<Invocation> invocationsInOrder = new TreeSet<Invocation>(new SequenceNumberComparator());
        for (Object mock : mocks) {
            InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
            List<Invocation> fromSingleMock = handler.getInvocationContainer().getInvocations();
            invocationsInOrder.addAll(fromSingleMock);
        }
        
        return new LinkedList<Invocation>(invocationsInOrder);
    }

    private final class SequenceNumberComparator implements Comparator<Invocation> {
        public int compare(Invocation o1, Invocation o2) {
            return Integer.valueOf(o1.getSequenceNumber()).compareTo(o2.getSequenceNumber());
        }
    }
}