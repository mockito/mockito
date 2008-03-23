/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.List;

import org.mockito.internal.invocation.GlobalInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.VerificationModeImpl;

public class VerifyingRecorder {

    private RegisteredInvocations registeredInvocations = new RegisteredInvocations();

    private final List<? extends Verifier> verifiers;
    private final GlobalInvocationsFinder globalInvocationsFinder;

    public VerifyingRecorder(GlobalInvocationsFinder globalInvocationsFinder, List<? extends Verifier> verifiers) {
        this.globalInvocationsFinder = globalInvocationsFinder;
        this.verifiers = verifiers;
    }

    public void recordInvocation(Invocation invocation) {
        registeredInvocations.add(invocation);
    }

    public void eraseLastInvocation() {
        registeredInvocations.removeLast();
    }

    public List<Invocation> getRegisteredInvocations() {
        return registeredInvocations.getVerifiableInvocations();
    }

    public void verify(VerificationModeImpl mode) {
        verify(null, mode);
    }

    public void verify(InvocationMatcher wanted, VerificationModeImpl mode) {
        List<Invocation> invocations;
        if (mode.inOrderMode()) {
            invocations = globalInvocationsFinder.getAllInvocations(mode.getMocksToBeVerifiedInOrder());
        } else {
            invocations = getRegisteredInvocations();
        }

        for (Verifier verifier : verifiers) {
            verifier.verify(invocations, wanted, mode);
        }
    }
}