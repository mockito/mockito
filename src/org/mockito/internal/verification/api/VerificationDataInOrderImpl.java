/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.api;

import java.util.List;

import org.mockito.internal.invocation.InvocationImpl;
import org.mockito.internal.invocation.InvocationMatcher;

public class VerificationDataInOrderImpl implements VerificationDataInOrder {

    private final InOrderContext inOrder;
    private final List<InvocationImpl> allInvocations;
    private final InvocationMatcher wanted;

    public VerificationDataInOrderImpl(InOrderContext inOrder, List<InvocationImpl> allInvocations, InvocationMatcher wanted) {
        this.inOrder = inOrder;
        this.allInvocations = allInvocations;
        this.wanted = wanted;        
    }

    public List<InvocationImpl> getAllInvocations() {
        return allInvocations;
    }

    public InOrderContext getOrderingContext() {
        return inOrder;
    }

    public InvocationMatcher getWanted() {
        return wanted;
    }
}