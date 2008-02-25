/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.HasStackTrace;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.progress.VerificationModeImpl;

public class NumberOfInvocationsVerifier implements Verifier {
    
    private final Reporter reporter;
    private final InvocationsFinder finder;

    public NumberOfInvocationsVerifier() {
        this(new Reporter(), new InvocationsFinder());
    }
    
    NumberOfInvocationsVerifier(Reporter reporter, InvocationsFinder finder) {
        this.reporter = reporter;
        this.finder = finder;
    }
    
    public void verify(List<Invocation> invocations, InvocationMatcher wanted, VerificationModeImpl mode) {
        if (!mode.exactNumberOfInvocationsMode()) {
            return;
        }
        
        List<Invocation> actualInvocations = finder.findInvocations(invocations, wanted, mode);
        
        int actualCount = actualInvocations.size();
        if (mode.tooLittleActualInvocations(actualCount)) {
            HasStackTrace lastInvocation = finder.getLastStackTrace(actualInvocations);
            reporter.tooLittleActualInvocations(mode.wantedCount(), actualCount, wanted, lastInvocation);
        } else if (mode.neverWantedButInvoked(actualCount)) {
            HasStackTrace firstUndesired = actualInvocations.get(mode.wantedCount()).getStackTrace();
            reporter.neverWantedButInvoked(wanted, firstUndesired); 
        } else if (mode.tooManyActualInvocations(actualCount)) {
            HasStackTrace firstUndesired = actualInvocations.get(mode.wantedCount()).getStackTrace();
            reporter.tooManyActualInvocations(mode.wantedCount(), actualCount, wanted, firstUndesired);
        }
        
        for (Invocation i : actualInvocations) {
            i.markVerified();
        }
    }
}
