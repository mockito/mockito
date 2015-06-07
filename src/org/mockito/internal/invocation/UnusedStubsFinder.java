/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.invocation;

import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.Invocation;

public class UnusedStubsFinder {

    /**
     * Finds all unused stubs for given mocks
     * 
     * @param mocks
     */
    public List<Invocation> find(final List<?> mocks) {
        final List<Invocation> unused = new LinkedList<Invocation>();
        for (final Object mock : mocks) {
            final InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
            final List<StubbedInvocationMatcher> fromSingleMock = handler.getInvocationContainer().getStubbedInvocations();
            for(final StubbedInvocationMatcher s : fromSingleMock) {
                if (!s.wasUsed()) {
                     unused.add(s.getInvocation());
                }
            }
        }
        return unused;
    }
}