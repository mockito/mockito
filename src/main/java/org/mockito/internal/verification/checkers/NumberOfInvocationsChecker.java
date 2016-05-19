/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification.checkers;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationsFinder;
import org.mockito.invocation.Invocation;

import java.util.List;

public class NumberOfInvocationsChecker {
    
    private final NumberOfInvocationsWithinRangeChecker withinRangeChecker;

    public NumberOfInvocationsChecker() {
        this.withinRangeChecker = new NumberOfInvocationsWithinRangeChecker();
    }
    
    NumberOfInvocationsChecker(Reporter reporter, InvocationsFinder finder) {
        this.withinRangeChecker = new NumberOfInvocationsWithinRangeChecker(reporter, finder);
    }
    
    public void check(List<Invocation> invocations, InvocationMatcher wanted, int wantedCount) {
        withinRangeChecker.check(invocations, wanted, wantedCount, wantedCount);
    }
}