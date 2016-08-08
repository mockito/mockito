/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.invocation.finder;

import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Stubbing;

import java.util.*;

public class AllInvocationsFinder {

    private AllInvocationsFinder() {}

    /**
     * gets all invocations from mocks. Invocations are ordered earlier first. 
     * 
     * @param mocks mocks
     * @return invocations
     */
    public static List<Invocation> find(List<?> mocks) {
        Set<Invocation> invocationsInOrder = new TreeSet<Invocation>(new SequenceNumberComparator());
        for (Object mock : mocks) {
            InternalMockHandler<Object> handler = MockUtil.getMockHandler(mock);
            List<Invocation> fromSingleMock = handler.getInvocationContainer().getInvocations();
            invocationsInOrder.addAll(fromSingleMock);
        }
        
        return new LinkedList<Invocation>(invocationsInOrder);
    }

    /**
     * gets all stubbings from mocks. Invocations are ordered earlier first.
     *
     * TODO 384 javadoc and unit tests
     *
     * @param mocks mocks
     * @return stubbings
     */
    public static Set<StubbedInvocationMatcher> findStubbings(Iterable<Object> mocks) {
        Set<StubbedInvocationMatcher> stubbings = new TreeSet<StubbedInvocationMatcher>(new SequenceNumberComparator2());
        for (Object mock : mocks) {
            InternalMockHandler<Object> handler = MockUtil.getMockHandler(mock);
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
            return Integer.valueOf(o1.getInvocation().getSequenceNumber()).compareTo(o2.getInvocation().getSequenceNumber());
        }
    }
}