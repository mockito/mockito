/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification.checkers;

import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationMarker;
import org.mockito.internal.invocation.InvocationsFinder;

public class AtLeastXNumberOfInvocationsInOrderChecker {
    
    private final Reporter reporter = new Reporter();
    private final InvocationsFinder finder = new InvocationsFinder();
    private final InvocationMarker invocationMarker = new InvocationMarker();
    
    public void check(List<Invocation> invocations, InvocationMatcher wanted, int wantedCount) {
        List<Invocation> chunk = finder.findAllMatchingUnverifiedChunks(invocations, wanted);
        
        int actualCount = chunk.size();
        
        if (wantedCount > actualCount) {
            Location lastLocation = finder.getLastLocation(chunk);
            reporter.tooLittleActualInvocationsInOrder(new AtLeastDiscrepancy(wantedCount, actualCount), wanted, lastLocation);
        }
        
        invocationMarker.markVerifiedInOrder(chunk, wanted);
    }
}