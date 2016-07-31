/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import java.util.Iterator;
import static org.mockito.internal.exceptions.Reporter.tooLittleActualInvocations;
import static org.mockito.internal.invocation.InvocationMarker.markVerified;
import static org.mockito.internal.invocation.InvocationsFinder.findInvocations;
import static org.mockito.internal.invocation.InvocationsFinder.getLastLocation;

import java.util.List;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class AtLeastXNumberOfInvocationsChecker {

    public void check(List<Invocation> invocations, InvocationMatcher wanted, int wantedCount) {
        List<Invocation> actualInvocations = findInvocations(invocations, wanted);
        
        int actualCount = actualInvocations.size();
        if (wantedCount > actualCount) {
            Location lastLocation = getLastLocation(actualInvocations);
            throw tooLittleActualInvocations(new AtLeastDiscrepancy(wantedCount, actualCount), wanted, lastLocation);
        }

        removeAlreadyVerified(actualInvocations);
        markVerified(actualInvocations, wanted);
    }

    private void removeAlreadyVerified(List<Invocation> invocations) {
        for (Iterator<Invocation> iterator = invocations.iterator(); iterator.hasNext(); ) {
            Invocation i = iterator.next();
            if (i.isVerified()) {
                iterator.remove();
            }
        }
    }
}
