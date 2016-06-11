/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import static org.mockito.exceptions.Reporter.tooLittleActualInvocations;

import java.util.List;

import org.mockito.internal.invocation.InvocationMarker;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class AtLeastXNumberOfInvocationsChecker {
    
    InvocationsFinder finder = new InvocationsFinder();
    InvocationMarker invocationMarker = new InvocationMarker();

    public void check(List<Invocation> invocations, InvocationMatcher wanted, int wantedCount) {
        List<Invocation> actualInvocations = finder.findInvocations(invocations, wanted);
        
        int actualCount = actualInvocations.size();
        if (wantedCount > actualCount) {
            Location lastLocation = finder.getLastLocation(actualInvocations);
            throw tooLittleActualInvocations(new AtLeastDiscrepancy(wantedCount, actualCount), wanted, lastLocation);        
        }
        
        invocationMarker.markVerified(actualInvocations, wanted);
    }
}