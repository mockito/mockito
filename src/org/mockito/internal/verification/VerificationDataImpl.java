/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.List;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.verification.api.VerificationData;

public class VerificationDataImpl implements VerificationData {

    private final List<Invocation> allInvocations;
    private final InvocationMatcher wanted;

    public VerificationDataImpl(List<Invocation> allInvocations, InvocationMatcher wanted) {
        this.allInvocations = allInvocations;
        this.wanted = wanted;
    }

    public List<Invocation> getAllInvocations() {
        return allInvocations;
    }

    public InvocationMatcher getWanted() {
        return wanted;
    }
}