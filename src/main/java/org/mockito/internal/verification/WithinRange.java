/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.verification;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.checkers.MissingInvocationChecker;
import org.mockito.internal.verification.checkers.NumberOfInvocationsWithinRangeChecker;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationMode;

import java.util.List;

public class WithinRange implements VerificationMode {

    private final int minNumberOfInvocations;
    private final int maxNumberOfInvocations;

    private final MissingInvocationChecker missingInvocationChecker = new MissingInvocationChecker();
    private final NumberOfInvocationsWithinRangeChecker invocationsWithinRangeChecker = new NumberOfInvocationsWithinRangeChecker();

    public WithinRange(int minNumberOfInvocations, int maxNumberOfInvocations) {
        if (minNumberOfInvocations < 0 || maxNumberOfInvocations < 0) {
            throw new MockitoException("Negative values are not allowed here");
        } else if (minNumberOfInvocations > maxNumberOfInvocations) {
            throw new MockitoException("Minimum number of invocations cannot be higher than max number of invocations");
        }
        this.minNumberOfInvocations = minNumberOfInvocations;
        this.maxNumberOfInvocations = maxNumberOfInvocations;
    }

    @Override
    public void verify(VerificationData data) {
        List<Invocation> invocations = data.getAllInvocations();
        InvocationMatcher wanted = data.getWanted();

        if (minNumberOfInvocations > 0) {
            missingInvocationChecker.check(invocations, wanted);
        }
        invocationsWithinRangeChecker.check(invocations, wanted, minNumberOfInvocations, maxNumberOfInvocations);
    }

    @Override
    public String toString() {
        return "Wanted invocations count: within range from " + minNumberOfInvocations + " to " + maxNumberOfInvocations;
    }

    @Override
    public VerificationMode description(String description) {
        return VerificationModeFactory.description(this, description);
    }
}
