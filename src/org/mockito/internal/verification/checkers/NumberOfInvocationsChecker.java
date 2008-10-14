/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.checkers;

import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.HasStackTrace;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsFinder;

public class NumberOfInvocationsChecker {
    
    private final Reporter reporter;
    private final InvocationsFinder finder;

    public NumberOfInvocationsChecker() {
        this(new Reporter(), new InvocationsFinder());
    }
    
    NumberOfInvocationsChecker(Reporter reporter, InvocationsFinder finder) {
        this.reporter = reporter;
        this.finder = finder;
    }
    
    public void check(List<Invocation> invocations, InvocationMatcher wanted, int wantedCount) {
        List<Invocation> actualInvocations = finder.findInvocations(invocations, wanted);
        
        int actualCount = actualInvocations.size();
        if (wantedCount > actualCount) {
            HasStackTrace lastInvocation = finder.getLastStackTrace(actualInvocations);
            reporter.tooLittleActualInvocations(wantedCount, actualCount, wanted, lastInvocation);
        } else if (wantedCount == 0 && actualCount > 0) {
            HasStackTrace firstUndesired = actualInvocations.get(wantedCount).getStackTrace();
            reporter.neverWantedButInvoked(wanted, firstUndesired); 
        } else if (wantedCount < actualCount) {
            HasStackTrace firstUndesired = actualInvocations.get(wantedCount).getStackTrace();
            reporter.tooManyActualInvocations(wantedCount, actualCount, wanted, firstUndesired);
        }
        
        for (Invocation i : actualInvocations) {
            i.markVerified();
        }
    }
}
