/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationsFinder;

public class AllInvocationsFinder implements InvocationsFinder {
    
    public List<Invocation> allInvocationsInOrder(List<Object> mocks) {
        List<Invocation> allInvocations = new LinkedList<Invocation>();
        for (Object mock : mocks) {
            List<Invocation> invocationsOfSingleMock = MockUtil.getControl(mock).getRegisteredInvocations();
            allInvocations.addAll(invocationsOfSingleMock);
        }
        return allInvocations;
    }
}
