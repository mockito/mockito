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

public class MissingInvocationInOrderVerifier implements Verifier {
    
    private final Reporter reporter;
    private final InvocationsFinder finder;
    
    public MissingInvocationInOrderVerifier() {
        this(new InvocationsFinder(), new Reporter());
    }
    
    public MissingInvocationInOrderVerifier(InvocationsFinder finder, Reporter reporter) {
        this.finder = finder;
        this.reporter = reporter;
    }

    public void verify(List<Invocation> invocations, InvocationMatcher wanted, VerificationModeImpl mode) {
        if (!mode.missingMethodInOrderMode()) {
            return;
        }
        
        List<Invocation> chunk = finder.findAllMatchingUnverifiedChunks(invocations, wanted);
        
        if (!chunk.isEmpty()) {
            return;
        }
        
        Invocation previousInOrder = finder.findPreviousVerifiedInOrder(invocations);
        if (previousInOrder == null) {
            reporter.wantedButNotInvoked(wanted);
        } else {
            reporter.wantedButNotInvokedInOrder(wanted, previousInOrder, previousInOrder.getStackTrace());
        }
    }
}