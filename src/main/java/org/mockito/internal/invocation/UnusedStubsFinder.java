/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.invocation;

import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.Invocation;

import java.util.*;

public class UnusedStubsFinder {

    /**
     * Finds all unused stubs for given mocks
     * 
     * @param mocks
     */
    public List<Invocation> find(List<?> mocks) {
        List<Invocation> unused = new LinkedList<Invocation>();
        for (Object mock : mocks) {
            InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
            List<StubbedInvocationMatcher> fromSingleMock = handler.getInvocationContainer().getStubbedInvocations();
            for(StubbedInvocationMatcher s : fromSingleMock) {
                if (!s.wasUsed()) {
                     unused.add(s.getInvocation());
                }
            }
        }
        return unused;
    }
}