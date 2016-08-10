/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.invocation.finder;

import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.Invocation;

import java.util.*;

public class AllInvocationsFinder {

    private AllInvocationsFinder() {}

    /**
     * gets all invocations from mocks. Invocations are ordered earlier first. 
     * 
     * @param mocks mocks
     * @return invocations
     */
    public static List<Invocation> find(Iterable<?> mocks) {
        //TODO 542 use the MockingDetails interface
        Set<Invocation> invocationsInOrder = new TreeSet<Invocation>(new SequenceNumberComparator());
        for (Object mock : mocks) {
            InternalMockHandler<Object> handler = MockUtil.getMockHandler(mock);
            List<Invocation> fromSingleMock = handler.getInvocationContainer().getInvocations();
            invocationsInOrder.addAll(fromSingleMock);
        }
        
        return new LinkedList<Invocation>(invocationsInOrder);
    }

    /**
     * Gets all stubbings from mocks. Invocations are ordered earlier first.
     *
     * @param mocks mocks
     * @return stubbings
     */
    public static Set<StubbedInvocationMatcher> findStubbings(Iterable<?> mocks) {
        Set<StubbedInvocationMatcher> stubbings = new TreeSet<StubbedInvocationMatcher>(new SequenceNumberComparator2());
        for (Object mock : mocks) {
            InternalMockHandler<Object> handler = MockUtil.getMockHandler(mock);
            //TODO 542 use the MockingDetails interface, add getStubbings() method
            List<StubbedInvocationMatcher> fromSingleMock = handler.getInvocationContainer().getStubbedInvocations();
            stubbings.addAll(fromSingleMock);
        }

        return stubbings;
    }

    private static final class SequenceNumberComparator implements Comparator<Invocation> {
        public int compare(Invocation o1, Invocation o2) {
            return Integer.valueOf(o1.getSequenceNumber()).compareTo(o2.getSequenceNumber());
        }
    }

    private static final class SequenceNumberComparator2 implements Comparator<StubbedInvocationMatcher> {
        public int compare(StubbedInvocationMatcher o1, StubbedInvocationMatcher o2) {
            return new SequenceNumberComparator().compare(o1.getInvocation(), o2.getInvocation());
        }
    }
}