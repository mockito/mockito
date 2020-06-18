/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import static org.mockito.internal.verification.checkers.MissingInvocationChecker.checkMissingInvocation;
import static org.mockito.internal.verification.checkers.NumberOfInvocationsChecker.checkNumberOfInvocationsNonGreedy;

import java.util.List;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MatchableInvocation;
import org.mockito.verification.VerificationMode;

public class Calls implements VerificationMode, VerificationInOrderMode {

    final int wantedCount;

    public Calls(int wantedNumberOfInvocations) {
        if (wantedNumberOfInvocations <= 0) {
            throw new MockitoException("Negative and zero values are not allowed here");
        }
        this.wantedCount = wantedNumberOfInvocations;
    }

    @Override
    public void verify(VerificationData data) {
        throw new MockitoException("calls is only intended to work with InOrder");
    }

    @Override
    public void verifyInOrder(VerificationDataInOrder data) {
        List<Invocation> allInvocations = data.getAllInvocations();
        MatchableInvocation wanted = data.getWanted();

        checkMissingInvocation(allInvocations, wanted, data.getOrderingContext());
        checkNumberOfInvocationsNonGreedy(
                allInvocations, wanted, wantedCount, data.getOrderingContext());
    }

    @Override
    public String toString() {
        return "Wanted invocations count (non-greedy): " + wantedCount;
    }
}
