/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.List;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.verification.api.VerificationMode;

//TODO is this class necessary?
public class VerifyingRecorder {

    private RegisteredInvocations registeredInvocations = new RegisteredInvocations();

    public void recordInvocation(Invocation invocation) {
        registeredInvocations.add(invocation);
    }

    public void eraseLastInvocation() {
        registeredInvocations.removeLast();
    }

    public List<Invocation> getRegisteredInvocations() {
        return registeredInvocations.getVerifiableInvocations();
    }

    public void verify(VerificationMode mode) {
        verify(null, mode);
    }

    public void verify(InvocationMatcher wanted, VerificationMode mode) {
        mode.verify(new VerificationDataImpl(getRegisteredInvocations(), wanted));
    }
}