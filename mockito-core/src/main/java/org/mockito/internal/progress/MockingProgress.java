/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import java.util.Set;

import org.mockito.invocation.Location;

import org.mockito.listeners.MockitoListener;
import org.mockito.listeners.VerificationListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationStrategy;

public interface MockingProgress {

    void reportOngoingStubbing(OngoingStubbing<?> ongoingStubbing);

    OngoingStubbing<?> pullOngoingStubbing();

    Set<VerificationListener> verificationListeners();

    void verificationStarted(VerificationMode verificationMode);

    VerificationMode pullVerificationMode();

    void stubbingStarted();

    void stubbingCompleted();

    void validateState();

    /**
     * Temporarily clears unfinished-stubbing tracking and returns the previous location.
     *
     * <p>Used so deep-stub path evaluation can run inside {@code doXxx().when(mock.path())}
     * without tripping unfinished-stubbing validation, while still restoring the flag so
     * misuse detection keeps working afterwards.
     *
     * @return previous stubbing location, or {@code null} when stubbing was not in progress
     */
    Location suspendStubbingInProgress();

    /**
     * Restores unfinished-stubbing tracking after {@link #suspendStubbingInProgress()}.
     *
     * @param location value returned by {@link #suspendStubbingInProgress()}, may be {@code null}
     */
    void resumeStubbingInProgress(Location location);

    void reset();

    /**
     * Removes ongoing stubbing so that in case the framework is misused
     * state validation errors are more accurate
     */
    void resetOngoingStubbing();

    ArgumentMatcherStorage getArgumentMatcherStorage();

    void mockingStarted(Object mock, MockCreationSettings<?> settings);

    void mockingStarted(Class<?> mock, MockCreationSettings<?> settings);

    void singletonMockingStarted(Object mock, MockCreationSettings<?> settings);

    void addListener(MockitoListener listener);

    void removeListener(MockitoListener listener);

    void setVerificationStrategy(VerificationStrategy strategy);

    VerificationMode maybeVerifyLazily(VerificationMode mode);

    /**
     * Removes all listeners added via {@link #addListener(MockitoListener)}.
     */
    void clearListeners();
}
