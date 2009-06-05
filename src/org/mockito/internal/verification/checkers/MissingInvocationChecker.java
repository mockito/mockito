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
import org.mockito.internal.reporting.SmartPrinter;

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
            if (similar != null) {
                SmartPrinter smartPrinter = new SmartPrinter(wanted, similar);
                reporter.argumentsAreDifferent(smartPrinter.getWanted(), smartPrinter.getActual(), similar.getLocation());
            } else {
                reporter.wantedButNotInvoked(wanted, invocations);
            }
        }
    }
}