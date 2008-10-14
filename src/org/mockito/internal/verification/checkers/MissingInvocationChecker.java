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
import org.mockito.internal.verification.SyncingPrinter;

public class MissingInvocationChecker {
    
    private final Reporter reporter;
    private final InvocationsFinder finder;
    
    public MissingInvocationChecker() {
        this(new InvocationsFinder(), new Reporter());
    }
    
    MissingInvocationChecker(InvocationsFinder finder, Reporter reporter) {
        this.finder = finder;
        this.reporter = reporter;
    }
    
    public void check(List<Invocation> invocations, InvocationMatcher wanted) {
        List<Invocation> actualInvocations = finder.findInvocations(invocations, wanted);
        
        if (actualInvocations.isEmpty()) {
            Invocation similar = finder.findSimilarInvocation(invocations, wanted);
            reportMissingInvocationError(wanted, similar);
        }
    }

    private void reportMissingInvocationError(InvocationMatcher wanted, Invocation similar) {
        if (similar != null) {
            SyncingPrinter syncingPrinter = new SyncingPrinter(wanted, similar);
            reporter.argumentsAreDifferent(syncingPrinter.getWanted(), syncingPrinter.getActual(), similar.getStackTrace());
        } else {
            reporter.wantedButNotInvoked(wanted);
        }
    }
}