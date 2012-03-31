/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.debugging.LocationImpl;
import org.mockito.internal.invocation.InvocationImpl;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationMarker;
import org.mockito.internal.invocation.InvocationsFinder;

public class AtLeastXNumberOfInvocationsChecker {
    
    Reporter reporter = new Reporter();
    InvocationsFinder finder = new InvocationsFinder();
    InvocationMarker invocationMarker = new InvocationMarker();

    public void check(List<InvocationImpl> invocations, InvocationMatcher wanted, int wantedCount) {
        List<InvocationImpl> actualInvocations = finder.findInvocations(invocations, wanted);
        
        int actualCount = actualInvocations.size();
        if (wantedCount > actualCount) {
            LocationImpl lastLocation = finder.getLastLocation(actualInvocations);
            reporter.tooLittleActualInvocations(new AtLeastDiscrepancy(wantedCount, actualCount), wanted, lastLocation);        
        }
        
        invocationMarker.markVerified(actualInvocations, wanted);
    }
}