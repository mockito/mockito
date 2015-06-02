/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.api;

import java.util.List;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;

public class VerificationDataInOrderImpl implements VerificationDataInOrder {

    private final InOrderContext inOrder;
    private final List<Invocation> allInvocations;
    private final InvocationMatcher wanted;

    public VerificationDataInOrderImpl(InOrderContext inOrder, List<Invocation> allInvocations, InvocationMatcher wanted) {
        this.inOrder = inOrder;
        this.allInvocations = allInvocations;
        this.wanted = wanted;        
    }

    public List<Invocation> getAllInvocations() {
        return allInvocations;
    }

    public InOrderContext getOrderingContext() {
        return inOrder;
    }

    public InvocationMatcher getWanted() {
        return wanted;
    }
}