/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.progress.VerificationModeImpl;

public class NoMoreInvocationsVerifier implements Verifier {

    private final Reporter reporter;
    private final InvocationsFinder finder;

    public NoMoreInvocationsVerifier() {
        this(new InvocationsFinder(), new Reporter());
    }
    
    public NoMoreInvocationsVerifier(InvocationsFinder finder, Reporter reporter) {
        this.finder = finder;
        this.reporter = reporter;
    }

    public void verify(List<Invocation> invocations, InvocationMatcher wanted, VerificationModeImpl mode) {
        if (mode.explicitMode()) {
            return;
        }

        Invocation unverified = finder.findFirstUnverified(invocations);
        if (unverified != null) {
            reporter.noMoreInteractionsWanted(unverified, unverified.getStackTrace());
        }
    }
}