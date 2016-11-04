/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.invocation.MatchableInvocation;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.invocation.Invocation;

import static org.mockito.internal.exceptions.Reporter.cannotVerifyToString;
import static org.mockito.internal.util.ObjectMethodsGuru.isToStringMethod;

import java.util.List;

public class VerificationDataImpl implements VerificationData {

    private final InvocationMatcher wanted;
    private final InvocationContainer invocations;

    public VerificationDataImpl(InvocationContainer invocations, InvocationMatcher wanted) {
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
