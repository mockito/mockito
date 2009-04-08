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
import org.mockito.internal.invocation.InvocationsFinder;

public class AtLeastXNumberOfInvocationsChecker {
    
    private final Reporter reporter = new Reporter();
    private final InvocationsFinder finder = new InvocationsFinder();

    public void check(List<Invocation> invocations, InvocationMatcher wanted, int wantedCount) {
        List<Invocation> actualInvocations = finder.findInvocations(invocations, wanted);
        
        int actualCount = actualInvocations.size();
        if (wantedCount > actualCount) {
            Location lastLocation = finder.getLastLocation(actualInvocations);
            reporter.tooLittleActualInvocations(new AtLeastDiscrepancy(wantedCount, actualCount), wanted, lastLocation);        
        }
        
        for (Invocation i : actualInvocations) {
            i.markVerified();
        }
    }
}