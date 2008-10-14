/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.checkers;

import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.verification.api.VerificationMode;

public class MissingInvocationInOrderChecker {
    
    private final Reporter reporter;
    private final InvocationsFinder finder;
    
    public MissingInvocationInOrderChecker() {
        this(new InvocationsFinder(), new Reporter());
    }
    
    MissingInvocationInOrderChecker(InvocationsFinder finder, Reporter reporter) {
        this.finder = finder;
        this.reporter = reporter;
    }
    
    public void check(List<Invocation> invocations, InvocationMatcher wanted, VerificationMode mode) {
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