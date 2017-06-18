/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.invocation;

import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MockHandler;
import org.mockito.stubbing.Stubbing;

import java.util.LinkedList;
import java.util.List;

@Deprecated
public class UnusedStubsFinder {

    /**
     * Finds all unused stubs for given mocks
     *
     * @param mocks full list of mocks
     */
    public List<Invocation> find(List<?> mocks) {
        List<Invocation> unused = new LinkedList<Invocation>();
        for (Object mock : mocks) {
            MockHandler<Object> handler = MockUtil.getMockHandler(mock);
            List<Stubbing> fromSingleMock = handler.getInvocationContainer().getStubbedInvocations();
            for(Stubbing s : fromSingleMock) {
                if (!s.wasUsed()) {
                     unused.add(s.getInvocation());
                }
            }
        }
        return unused;
    }
}
