/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;

import java.util.List;

import static org.mockito.internal.exceptions.Reporter.cannotVerifyToString;
import static org.mockito.internal.util.ObjectMethodsGuru.isToStringMethod;

public class VerificationDataImpl implements VerificationData {

    private final InvocationMatcher wanted;
    private final InvocationContainerImpl invocations;

    public VerificationDataImpl(InvocationContainerImpl invocations, InvocationMatcher wanted) {
        this.invocations = invocations;
        this.wanted = wanted;
        this.assertWantedIsVerifiable();
    }

    @Override
    public List<Invocation> getAllInvocations() {
        return invocations.getInvocations();
    }

    @Override
    public MatchableInvocation getTarget() {
        return wanted;
    }

    @Override
    public InvocationMatcher getWanted() {
        return wanted;
    }

    private void assertWantedIsVerifiable() {
        if (wanted == null) {
            return;
        }
        if (isToStringMethod(wanted.getMethod())) {
            throw cannotVerifyToString();
        }
    }
}
