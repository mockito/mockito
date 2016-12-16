/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.progress;

import org.mockito.MockitoLambda;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.listeners.MockitoListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationStrategy;

public interface MockingProgress {
    
    OngoingStubbing<?> pullOngoingStubbing();

    void verificationStarted(VerificationMode verificationMode);

    VerificationMode pullVerificationMode();

    void stubbingStarted();

    void stubbingCompleted();
    
    void validateState();

    void reset();

    /**
     * Removes ongoing stubbing so that in case the framework is misused
     * state validation errors are more accurate
     */
    void resetInvocationContainer();

    ArgumentMatcherStorage getArgumentMatcherStorage();
    
    void mockingStarted(Object mock, MockCreationSettings settings);

    void addListener(MockitoListener listener);

    void removeListener(MockitoListener listener);

    void setVerificationStrategy(VerificationStrategy strategy);

    VerificationMode maybeVerifyLazily(VerificationMode mode);

    <R, A extends MockitoLambda.Answer<R>> MockitoLambda.FinishableStubbing<R,A> pullFinishableStubbing();

    <R, A extends MockitoLambda.Answer<R>> MockitoLambda.FinishableAnswer<R,A> finishableAnswer();

    void reportInvocation(InvocationContainerImpl invocationContainer);
}