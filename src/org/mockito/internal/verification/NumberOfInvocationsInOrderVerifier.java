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

public class NumberOfInvocationsInOrderVerifier {
    
    private final Reporter reporter;
    private final InvocationsFinder finder;
    
    public NumberOfInvocationsInOrderVerifier() {
        this(new InvocationsFinder(), new Reporter());
    }
    
    public NumberOfInvocationsInOrderVerifier(InvocationsFinder finder, Reporter reporter) {
        this.finder = finder;
        this.reporter = reporter;
    }
    
    public void verify(List<Invocation> invocations, InvocationMatcher wanted, MockitoVerificationMode mode) {
        VerificationModeDecoder decoder = new VerificationModeDecoder(mode);
        List<Invocation> chunk = finder.findMatchingChunk(invocations, wanted, mode);
        
        boolean noMatchFound = chunk.size() == 0;
        if (decoder.neverWanted() && noMatchFound) {
            return;
        }
        
        int actualCount = chunk.size();
        
        if (decoder.tooLittleActualInvocations(actualCount)) {
            HasStackTrace lastInvocation = finder.getLastStackTrace(chunk);
            reporter.tooLittleActualInvocationsInOrder(mode.wantedCount(), actualCount, wanted, lastInvocation);
        } else if (decoder.tooLittleActualInvocationsInAtLeastMode(actualCount)) {
            HasStackTrace lastInvocation = finder.getLastStackTrace(chunk);
            reporter.tooLittleActualInvocationsInOrderInAtLeastMode(mode.wantedCount(), actualCount, wanted, lastInvocation);
        } else if (decoder.tooManyActualInvocations(actualCount)) {
            HasStackTrace firstUndesired = chunk.get(mode.wantedCount()).getStackTrace();
            reporter.tooManyActualInvocationsInOrder(mode.wantedCount(), actualCount, wanted, firstUndesired);
        }
        
        for (Invocation i : chunk) {
            i.markVerifiedInOrder();
        }
    }
}