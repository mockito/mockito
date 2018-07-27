/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.verification;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationInOrderMode;
import org.mockito.internal.verification.within.AtLeast;
import org.mockito.internal.verification.within.AtLeastAndAtMost;
import org.mockito.internal.verification.within.AtMost;
import org.mockito.internal.verification.within.Only;
import org.mockito.internal.verification.within.Times;
import org.mockito.internal.verification.within.VerificationStrategy;
import org.mockito.internal.verification.within.WithinVerfication;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class Within implements VerificationMode, VerificationInOrderMode, VerificationAfterDelay {

    private final long deadLine;

    Within(long deadLine) {
        this.deadLine = deadLine;
    }

    public static Within untilNow() {
        return within(0, NANOSECONDS);
    }

    public static Within within(long duration, TimeUnit unit) {
        long deadLine = System.nanoTime() + unit.toNanos(duration);
        return new Within(deadLine);
    }

    @Override
    public void verify(VerificationData data) {
        times(1).verify(data);
    }

    @Override
    public void verifyInOrder(VerificationDataInOrder data) {
        ((VerificationInOrderMode) times(1)).verifyInOrder(data);

    }

    @Override
    public VerificationMode description(String description) {
        return this;
    }

    public VerificationModeAtLeast atLeast(int minNumberOfInvocations) {
        if (minNumberOfInvocations <= 0) {
            throw new MockitoException("The minimum number of invocations must be greater that 0! Got: " + minNumberOfInvocations + "\r\nIf you want to verify that nothing was called use Mocktio.never() instead!");
        }

        return new WithinAtLeast(minNumberOfInvocations);
    }

    public VerificationMode atMost(int maxNumberOfInvocations) {
        return mode(new AtMost(maxNumberOfInvocations));
    }

    public VerificationMode times(int exactNumberOfInvocations) {
        return mode(new Times(exactNumberOfInvocations));
    }

    public VerificationMode never() {
        return mode(new Times(0));
    }

    @Override
    public VerificationMode atLeastOnce() {
        return atLeast(1);
    }

    public VerificationMode only() {
        return mode(new Only());
    }

    private WithinVerfication mode(VerificationStrategy strategy) {
        return new WithinVerfication(deadLine, strategy);
    }

    private class WithinAtLeast implements VerificationModeAtLeast {
        private int minNumberOfInvocations;

        public WithinAtLeast(int minNumberOfInvocations) {
            this.minNumberOfInvocations = minNumberOfInvocations;
        }

        @Override
        public void verify(VerificationData data) {
            mode(new AtLeast(minNumberOfInvocations)).verify(data);
        }

        @Override
        public VerificationMode description(String description) {
            return this;
        }

        @Override
        public VerificationMode andAtMost(int maxNumberOfInvocations) {
            AtLeastAndAtMost strategy = new AtLeastAndAtMost(minNumberOfInvocations, maxNumberOfInvocations);
            return mode(strategy);
        }
    }
}
