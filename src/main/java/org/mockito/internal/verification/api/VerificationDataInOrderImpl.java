/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.api;

import java.util.List;

import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;

public class VerificationDataInOrderImpl implements VerificationDataInOrder {

    private final InOrderContext inOrder;
    private final List<Invocation> allInvocations;
    private final MatchableInvocation wanted;

    public VerificationDataInOrderImpl(InOrderContext inOrder, List<Invocation> allInvocations, MatchableInvocation wanted) {
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

    public MatchableInvocation getWanted() {
        return wanted;
    }
}
