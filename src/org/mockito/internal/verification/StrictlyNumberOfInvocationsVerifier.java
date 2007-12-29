/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.HasStackTrace;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.VerificationModeImpl;

public class StrictlyNumberOfInvocationsVerifier implements Verifier {
    
    private final Reporter reporter;
    private final InvocationsFinder finder;
    
    public StrictlyNumberOfInvocationsVerifier() {
        this(new InvocationsFinder(), new Reporter());
    }
    
    public StrictlyNumberOfInvocationsVerifier(InvocationsFinder finder, Reporter reporter) {
        this.finder = finder;
        this.reporter = reporter;
    }

    public void verify(List<Invocation> invocations, InvocationMatcher wanted, VerificationModeImpl mode) {
        if (!mode.strictMode()) {
            return;
        }
        
        List<Invocation> chunk = finder.findFirstUnverifiedChunk(invocations, wanted);
        
        boolean noMatchFound = chunk.size() == 0 || !wanted.matches(chunk.get(0));
        if (mode.wantedCountIsZero() && noMatchFound) {
            return;
        }
        
        int actualCount = chunk.size();
        
        if (mode.tooLittleActualInvocations(actualCount)) {
            HasStackTrace lastInvocation = finder.getLastStackTrace(chunk);
            reporter.strictlyTooLittleActualInvocations(mode.wantedCount(), actualCount, wanted.toString(), lastInvocation);
        }
        
        if (mode.tooManyActualInvocations(actualCount)) {
            HasStackTrace firstUndesired = chunk.get(mode.wantedCount()).getStackTrace();
            reporter.strictlyTooManyActualInvocations(mode.wantedCount(), actualCount, wanted.toString(), firstUndesired);
        }
        
        for (Invocation i : chunk) {
            i.markVerifiedStrictly();
        }
    }
}