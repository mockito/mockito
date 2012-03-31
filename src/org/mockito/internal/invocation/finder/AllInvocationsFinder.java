/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.invocation.finder;

import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.invocation.InvocationImpl;
import org.mockito.internal.util.MockUtil;

import java.util.*;

public class AllInvocationsFinder {
    
    /**
     * gets all invocations from mocks. Invocations are ordered earlier first. 
     * 
     * @param mocks mocks
     * @return invocations
     */
    public List<InvocationImpl> find(List<?> mocks) {
        Set<InvocationImpl> invocationsInOrder = new TreeSet<InvocationImpl>(new SequenceNumberComparator());
        for (Object mock : mocks) {
            MockHandlerInterface<Object> handler = new MockUtil().getMockHandler(mock);
            List<InvocationImpl> fromSingleMock = handler.getInvocationContainer().getInvocations();
            invocationsInOrder.addAll(fromSingleMock);
        }
        
        return new LinkedList<InvocationImpl>(invocationsInOrder);
    }

    private final class SequenceNumberComparator implements Comparator<InvocationImpl> {
        public int compare(InvocationImpl o1, InvocationImpl o2) {
            return Integer.valueOf(o1.getSequenceNumber()).compareTo(o2.getSequenceNumber());
        }
    }
}