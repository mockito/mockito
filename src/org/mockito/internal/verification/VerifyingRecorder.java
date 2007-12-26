/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.GlobalInvocationsFinder;
import org.mockito.internal.progress.VerificationModeImpl;

public class VerifyingRecorder {

    private LinkedList<Invocation> registeredInvocations = new LinkedList<Invocation>();

    private final List<? extends Verifier> verifiers;
    private final GlobalInvocationsFinder globalInvocationsFinder;

    public VerifyingRecorder(GlobalInvocationsFinder globalInvocationsFinder, List<? extends Verifier> verifiers) {
        this.globalInvocationsFinder = globalInvocationsFinder;
        this.verifiers = verifiers;
    }

    public void recordInvocation(Invocation invocation) {
        this.registeredInvocations.add(invocation);
    }

    public void eraseLastInvocation() {
        registeredInvocations.removeLast();
    }

    public List<Invocation> getRegisteredInvocations() {
        return registeredInvocations;
    }

    public void verify(VerificationModeImpl mode) {
        verify(null, mode);
    }

    public void verify(InvocationMatcher wanted, VerificationModeImpl mode) {
        List<Invocation> invocations;
        if (mode.strictMode()) {
            invocations = globalInvocationsFinder.getAllInvocations(mode.getMocksToBeVerifiedStrictly());
        } else {
            invocations = registeredInvocations;
        }

        for (Verifier verifier : verifiers) {
            verifier.verify(invocations, wanted, mode);
        }
    }
}