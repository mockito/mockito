/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.progress.OngoingVerifyingMode;

public class AllInvocationsFinder implements InvocationsFinder {
    
    private final OngoingVerifyingMode mode;

    //TODO name of invocations finder
    public AllInvocationsFinder(OngoingVerifyingMode mode) {
        this.mode = mode;
    }

    public List<Invocation> allInvocationsInOrder() {
        List<Object> mocks = mode.getAllMocksToBeVerifiedInSequence();
        List<Invocation> allInvocations = new LinkedList<Invocation>();
        for (Object mock : mocks) {
            List<Invocation> invocationsOfSingleMock = MockUtil.getControl(mock).getRegisteredInvocations();
            allInvocations.addAll(invocationsOfSingleMock);
        }
        return allInvocations;
    }
}
