/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import static org.mockito.exceptions.Reporter.neverWantedButInvoked;
import static org.mockito.exceptions.Reporter.tooLittleActualInvocations;
import static org.mockito.exceptions.Reporter.tooManyActualInvocations;

import java.util.List;

import org.mockito.internal.invocation.InvocationMarker;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.reporting.Discrepancy;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class NumberOfInvocationsChecker {
    
    private final InvocationsFinder finder=new InvocationsFinder();
    private final InvocationMarker invocationMarker = new InvocationMarker();

    
    public void check(List<Invocation> invocations, InvocationMatcher wanted, int wantedCount) {
        List<Invocation> actualInvocations = finder.findInvocations(invocations, wanted);
        
        int actualCount = actualInvocations.size();
        if (wantedCount > actualCount) {
            Location lastInvocation = finder.getLastLocation(actualInvocations);
            throw tooLittleActualInvocations(new Discrepancy(wantedCount, actualCount), wanted, lastInvocation);
        } 
        if (wantedCount == 0 && actualCount > 0) {
            Location firstUndesired = actualInvocations.get(wantedCount).getLocation();
            throw neverWantedButInvoked(wanted, firstUndesired); 
        } 
        if (wantedCount < actualCount) {
            Location firstUndesired = actualInvocations.get(wantedCount).getLocation();
            throw tooManyActualInvocations(wantedCount, actualCount, wanted, firstUndesired);
        }
        
        invocationMarker.markVerified(actualInvocations, wanted);
    }
}