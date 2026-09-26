/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

import org.mockito.ArgumentMatcher;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.MatchableInvocation;
import org.mockito.verification.VerificationMode;

/**
 * Verifies that another verification mode (the delegate) is satisfied within a certain timeframe
 * (before timeoutMillis has passed, measured from the call to verify()), and either returns immediately
 * once it does, or waits until it is definitely satisfied once the full time has passed.
 */
public class VerificationOverTimeImpl implements VerificationMode {

    private final long pollingPeriodMillis;
    private final VerificationMode delegate;
    private final boolean returnOnSuccess;
    private final Timer timer;

    /**
     * Create this verification mode, to be used to verify invocation ongoing data later.
     *
     * @param pollingPeriodMillis The frequency to poll delegate.verify(), to check whether the delegate has been satisfied
     * @param durationMillis The max time to wait (in millis) for the delegate verification mode to be satisfied
     * @param delegate The verification mode to delegate overall success or failure to
     * @param returnOnSuccess Whether to immediately return successfully once the delegate is satisfied (as in
     *                        {@link org.mockito.verification.VerificationWithTimeout}, or to only return once
     *                        the delegate is satisfied and the full duration has passed (as in
     *                        {@link org.mockito.verification.VerificationAfterDelay}).
     */
    public VerificationOverTimeImpl(
            long pollingPeriodMillis,
            long durationMillis,
            VerificationMode delegate,
            boolean returnOnSuccess) {
        this(pollingPeriodMillis, delegate, returnOnSuccess, new Timer(durationMillis));
    }

    /**
     * Create this verification mode, to be used to verify invocation ongoing data later.
     *
     * @param pollingPeriodMillis The frequency to poll delegate.verify(), to check whether the delegate has been satisfied
     * @param delegate The verification mode to delegate overall success or failure to
     * @param returnOnSuccess Whether to immediately return successfully once the delegate is satisfied (as in
     *                        {@link org.mockito.verification.VerificationWithTimeout}, or to only return once
     *                        the delegate is satisfied and the full duration has passed (as in
     *                        {@link org.mockito.verification.VerificationAfterDelay}).
     * @param timer Checker of whether the duration of the verification is still acceptable
     */
    public VerificationOverTimeImpl(
            long pollingPeriodMillis,
            VerificationMode delegate,
            boolean returnOnSuccess,
            Timer timer) {
        this.pollingPeriodMillis = pollingPeriodMillis;
        this.delegate = delegate;
        this.returnOnSuccess = returnOnSuccess;
        this.timer = timer;
    }

    /**
     * Verify the given ongoing verification data, and confirm that it satisfies the delegate verification mode
     * before the full duration has passed.
     *
     * In practice, this polls the delegate verification mode until it is satisfied. If it is not satisfied once
     * the full duration has passed, the last error returned by the delegate verification mode will be thrown
     * here in turn. This may be thrown early if the delegate is unsatisfied and the verification mode is known
     * to never recover from this situation (e.g. {@link AtMost}).
     *
     * If it is satisfied before the full duration has passed, behaviour is dependent on the returnOnSuccess parameter
     * given in the constructor. If true, this verification mode is immediately satisfied once the delegate is. If
     * false, this verification mode is not satisfied until the delegate is satisfied and the full time has passed.
     *
     * Because the delegate is polled repeatedly, and evaluating the delegate captures arguments as a side effect,
     * the data is wrapped so that the arguments of any given invocation are captured at most once, no matter how
     * many times that invocation is seen by a poll.
     *
     * @throws MockitoAssertionError if the delegate verification mode does not succeed before the timeout
     */
    @Override
    public void verify(VerificationData data) {
        AssertionError error = null;

        VerificationData polledData = new CaptureOncePerInvocationData(data);

        timer.start();
        while (timer.isCounting()) {
            try {
                delegate.verify(polledData);

                if (returnOnSuccess) {
                    return;
                } else {
                    error = null;
                }
            } catch (AssertionError e) {
                error = handleVerifyException(e);
            }
        }

        if (error != null) {
            throw error;
        }
    }

    private AssertionError handleVerifyException(AssertionError e) {
        if (canRecoverFromFailure(delegate)) {
            sleep(pollingPeriodMillis);
            return e;
        } else {
            throw e;
        }
    }

    protected boolean canRecoverFromFailure(VerificationMode verificationMode) {
        return !(verificationMode instanceof AtMost
                || verificationMode instanceof NoMoreInteractions);
    }

    public VerificationOverTimeImpl copyWithVerificationMode(VerificationMode verificationMode) {
        return new VerificationOverTimeImpl(
                pollingPeriodMillis, timer.duration(), verificationMode, returnOnSuccess);
    }

    private void sleep(long sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException ie) {
            throw new RuntimeException("Thread sleep has been interrupted", ie);
        }
    }

    public boolean isReturnOnSuccess() {
        return returnOnSuccess;
    }

    public long getPollingPeriodMillis() {
        return pollingPeriodMillis;
    }

    public Timer getTimer() {
        return timer;
    }

    public VerificationMode getDelegate() {
        return delegate;
    }

    /**
     * Verification data that hands out a target which captures the arguments of any given invocation
     * at most once, see {@link CaptureOncePerInvocation}.
     */
    private static class CaptureOncePerInvocationData implements VerificationData {

        private final VerificationData delegate;
        private final MatchableInvocation target;

        CaptureOncePerInvocationData(VerificationData delegate) {
            this.delegate = delegate;
            this.target = new CaptureOncePerInvocation(delegate.getTarget());
        }

        @Override
        public List<Invocation> getAllInvocations() {
            return delegate.getAllInvocations();
        }

        @Override
        public MatchableInvocation getTarget() {
            return target;
        }
    }

    /**
     * Delegates to another {@link MatchableInvocation}, but captures the arguments of any given
     * invocation only the first time it is asked to.
     *
     * Verification over time polls the delegate verification mode repeatedly. Capturing arguments is a
     * side effect of evaluating a verification mode, so without this guard an {@link org.mockito.ArgumentCaptor}
     * would collect one copy of every argument per poll rather than one per invocation, and a long
     * enough duration could exhaust the heap.
     *
     * Invocations are compared by identity: an invocation that is genuinely recorded twice is a
     * different instance and is captured twice, as it should be.
     */
    private static class CaptureOncePerInvocation implements MatchableInvocation {

        private final MatchableInvocation delegate;
        private final Set<Invocation> alreadyCaptured =
                Collections.newSetFromMap(new IdentityHashMap<>());

        CaptureOncePerInvocation(MatchableInvocation delegate) {
            this.delegate = delegate;
        }

        @Override
        public void captureArgumentsFrom(Invocation invocation) {
            if (alreadyCaptured.add(invocation)) {
                delegate.captureArgumentsFrom(invocation);
            }
        }

        @Override
        public Invocation getInvocation() {
            return delegate.getInvocation();
        }

        @Override
        public List<ArgumentMatcher> getMatchers() {
            return delegate.getMatchers();
        }

        @Override
        public boolean matches(Invocation candidate) {
            return delegate.matches(candidate);
        }

        @Override
        public boolean hasSimilarMethod(Invocation candidate) {
            return delegate.hasSimilarMethod(candidate);
        }

        @Override
        public boolean hasSameMethod(Invocation candidate) {
            return delegate.hasSameMethod(candidate);
        }

        @Override
        public Location getLocation() {
            return delegate.getLocation();
        }

        @Override
        public String toString() {
            return delegate.toString();
        }
    }
}
