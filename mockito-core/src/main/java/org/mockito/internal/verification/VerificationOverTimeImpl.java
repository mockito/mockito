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

    private final VerificationMode delegate;
    private final boolean returnOnSuccess;
    private final Timer timer;

    /**
     * Create this verification mode, to be used to verify invocation ongoing data later.
     *
     * @param durationMillis The max time to wait (in millis) for the delegate verification mode to be satisfied
     * @param delegate The verification mode to delegate overall success or failure to
     * @param returnOnSuccess Whether to immediately return successfully once the delegate is satisfied (as in
     *                        {@link org.mockito.verification.VerificationWithTimeout}, or to only return once
     *                        the delegate is satisfied and the full duration has passed (as in
     *                        {@link org.mockito.verification.VerificationAfterDelay}).
     */
    public VerificationOverTimeImpl(
            long durationMillis, VerificationMode delegate, boolean returnOnSuccess) {
        this(delegate, returnOnSuccess, new Timer(durationMillis));
    }

    /**
     * Create this verification mode, to be used to verify invocation ongoing data later.
     *
     * @param delegate The verification mode to delegate overall success or failure to
     * @param returnOnSuccess Whether to immediately return successfully once the delegate is satisfied (as in
     *                        {@link org.mockito.verification.VerificationWithTimeout}, or to only return once
     *                        the delegate is satisfied and the full duration has passed (as in
     *                        {@link org.mockito.verification.VerificationAfterDelay}).
     * @param timer Checker of whether the duration of the verification is still acceptable
     */
    public VerificationOverTimeImpl(
            VerificationMode delegate, boolean returnOnSuccess, Timer timer) {
        this.delegate = delegate;
        this.returnOnSuccess = returnOnSuccess;
        this.timer = timer;
    }

    /**
     * Verify the given ongoing verification data, and confirm that it satisfies the delegate verification mode
     * before the full duration has passed.
     *
     * The delegate verification mode is evaluated once straight away, and then again each time the invocations
     * registered on a mock change, see {@link InvocationSignal}. In between, the verifying thread waits rather
     * than polling. One final pass always runs once the full duration has passed, because the outcome can change
     * without a new invocation, for example when an argument is mutated after the call, when a matcher reads
     * external state, or when the verifying thread wakes up late on a busy machine.
     *
     * If the delegate is not satisfied once the full duration has passed, the error from that final pass is thrown
     * here in turn. This may be thrown early if the delegate is unsatisfied and the verification mode is known to
     * never recover from this situation (e.g. {@link AtMost}).
     *
     * If it is satisfied before the full duration has passed, behaviour is dependent on the returnOnSuccess parameter
     * given in the constructor. If true, this verification mode is immediately satisfied once the delegate is. If
     * false, this verification mode is not satisfied until the delegate is satisfied and the full time has passed.
     *
     * Because the delegate can be evaluated more than once, and evaluating the delegate captures arguments as a
     * side effect, the data is wrapped so that the arguments of any given invocation are captured at most once, no
     * matter how many passes see that invocation.
     *
     * If the verifying thread is interrupted while it waits, the verification stops and the thread is left
     * interrupted. An interrupt that is already pending when the verification starts does not stop it: it says
     * nothing about the invocations being verified, so it is cleared for the duration and restored afterwards.
     *
     * @throws MockitoAssertionError if the delegate verification mode does not succeed before the timeout
     */
    @Override
    public void verify(VerificationData data) {
        boolean interruptPending = Thread.interrupted();
        try {
            verifyOverTime(data);
        } finally {
            if (interruptPending) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void verifyOverTime(VerificationData data) {
        AssertionError error = null;

        VerificationData passData = new CaptureOncePerInvocationData(data);

        timer.start();
        while (true) {
            boolean finalPass = !timer.isCounting();
            InvocationSignal.Pass pass = InvocationSignal.startPass();
            try {
                delegate.verify(passData);

                if (returnOnSuccess) {
                    return;
                } else {
                    error = null;
                }
            } catch (AssertionError e) {
                error = handleVerifyException(e);
            } finally {
                pass.end();
            }
            if (finalPass) {
                break;
            }
            pass.awaitChange(timer);
        }

        if (error != null) {
            throw error;
        }
    }

    private AssertionError handleVerifyException(AssertionError e) {
        if (canRecoverFromFailure(delegate)) {
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
        return new VerificationOverTimeImpl(timer.duration(), verificationMode, returnOnSuccess);
    }

    public boolean isReturnOnSuccess() {
        return returnOnSuccess;
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
     * Verification over time can evaluate the delegate verification mode more than once. Capturing arguments
     * is a side effect of evaluating a verification mode, so without this guard an
     * {@link org.mockito.ArgumentCaptor} would collect one copy of every argument per pass rather than one per
     * invocation.
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
