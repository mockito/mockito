/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import java.util.List;
import org.mockito.internal.reporting.Discrepancy;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.MatchableInvocation;

import static org.mockito.internal.exceptions.Reporter.neverWantedButInvoked;
import static org.mockito.internal.exceptions.Reporter.tooLittleActualInvocations;
import static org.mockito.internal.exceptions.Reporter.tooManyActualInvocations;
import static org.mockito.internal.invocation.InvocationMarker.markVerified;
import static org.mockito.internal.invocation.InvocationsFinder.findInvocations;
import static org.mockito.internal.invocation.InvocationsFinder.getLastLocation;

public class NumberOfInvocationsChecker {

    public void check(List<Invocation> invocations, MatchableInvocation wanted, int wantedCount) {
        List<Invocation> actualInvocations = findInvocations(invocations, wanted);
        
        int actualCount = actualInvocations.size();
        if (wantedCount > actualCount) {
            Location lastInvocation = getLastLocation(actualInvocations);
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

        markVerified(actualInvocations, wanted);
    }
}
