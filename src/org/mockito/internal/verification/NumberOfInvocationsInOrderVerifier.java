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

public class NumberOfInvocationsInOrderVerifier implements Verifier {
    
    private final Reporter reporter;
    private final InvocationsFinder finder;
    
    public NumberOfInvocationsInOrderVerifier() {
        this(new InvocationsFinder(), new Reporter());
    }
    
    public NumberOfInvocationsInOrderVerifier(InvocationsFinder finder, Reporter reporter) {
        this.finder = finder;
        this.reporter = reporter;
    }

    public void verify(List<Invocation> invocations, InvocationMatcher wanted, VerificationModeImpl mode) {
        if (!mode.inOrderMode()) {
            return;
        }
        
        List<Invocation> chunk = finder.findFirstMatchingChunk(invocations, wanted);
        
        boolean noMatchFound = chunk.size() == 0;
        if (mode.wantedCountIsZero() && noMatchFound) {
            return;
        }
        
        int actualCount = chunk.size();

        if (mode.atLeastOnceMode() || !mode.matchesActualCount(actualCount)) {
            //try to match on all chunks
            chunk = finder.findAllMatchingUnverifiedChunks(invocations, wanted);
        }
        
        int actualInAll = chunk.size();
            
        if (mode.tooLittleActualInvocations(actualInAll)) {
            HasStackTrace lastInvocation = finder.getLastStackTrace(chunk);
            reporter.tooLittleActualInvocationsInOrder(mode.wantedCount(), actualInAll, wanted.toString(), lastInvocation);
        }
        
        if (mode.tooManyActualInvocations(actualInAll)) {
            HasStackTrace firstUndesired = chunk.get(mode.wantedCount()).getStackTrace();
            reporter.tooManyActualInvocationsInOrder(mode.wantedCount(), actualInAll, wanted.toString(), firstUndesired);
        }
        
        for (Invocation i : chunk) {
            i.markVerifiedInOrder();
        }
    }
}