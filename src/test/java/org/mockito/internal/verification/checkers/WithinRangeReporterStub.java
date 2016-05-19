/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import org.mockito.exceptions.Reporter;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Location;

public class WithinRangeReporterStub extends Reporter {

    int wantedCount;
    int actualCount;
    DescribedInvocation wanted;
    Location location;

    @Override public void tooLittleActualInvocations(org.mockito.internal.reporting.Discrepancy discrepancy, DescribedInvocation wanted, Location lastActualLocation) {
        this.wantedCount = discrepancy.getWantedCount();
        this.actualCount = discrepancy.getActualCount();
        this.wanted = wanted;
        this.location = lastActualLocation;
    }

    @Override public void tooManyActualInvocations(int wantedCount, int actualCount, DescribedInvocation wanted, Location firstUndesired) {
        this.wantedCount = wantedCount;
        this.actualCount = actualCount;
        this.wanted = wanted;
        this.location = firstUndesired;
    }

    @Override
    public void neverWantedButInvoked(DescribedInvocation wanted, Location firstUndesired) {
        this.wanted = wanted;
        this.location = firstUndesired;
    }
}
