/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationMarker;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.internal.reporting.Discrepancy;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import java.util.List;

public class NumberOfInvocationsWithinRangeChecker {

    private final Reporter reporter;
    private final InvocationsFinder finder;
    private final InvocationMarker invocationMarker = new InvocationMarker();

    public NumberOfInvocationsWithinRangeChecker() {
        this(new Reporter(), new InvocationsFinder());
    }

    NumberOfInvocationsWithinRangeChecker(Reporter reporter, InvocationsFinder finder) {
        this.reporter = reporter;
        this.finder = finder;
    }

    public void check(List<Invocation> invocations, InvocationMatcher wanted, int minNumberOfInvocations, int maxNumberOfInvocations) {
        List<Invocation> actualInvocations = finder.findInvocations(invocations, wanted);

        int actualCount = actualInvocations.size();
        if (actualCount < minNumberOfInvocations) {
            Location lastInvocation = finder.getLastLocation(actualInvocations);
            reporter.tooLittleActualInvocations(new Discrepancy(minNumberOfInvocations, actualCount), wanted, lastInvocation);
        } else if (maxNumberOfInvocations == 0 && actualCount > 0) {
            Location firstUndesired = actualInvocations.get(maxNumberOfInvocations).getLocation();
            reporter.neverWantedButInvoked(wanted, firstUndesired);
        } else if (actualCount > maxNumberOfInvocations) {
            Location firstUndesired = actualInvocations.get(maxNumberOfInvocations).getLocation();
            reporter.tooManyActualInvocations(maxNumberOfInvocations, actualCount, wanted, firstUndesired);
        }

        invocationMarker.markVerified(actualInvocations, wanted);
    }
}
