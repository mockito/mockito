/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.Primitives;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationContainer;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

import java.util.Collections;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

public class MockitoLambdaHandlerImpl<T> implements MockHandler<T> {

    private final MockCreationSettings<T> settings;

    private final MatchersBinder matchersBinder;

    private final InvocationContainerImpl invocationContainer;

    static Answer<?> answerValue;
    static VerificationMode verificationMode;

    MockitoLambdaHandlerImpl(MockCreationSettings<T> settings) {
        this.settings = settings;
        this.matchersBinder = new MatchersBinder();
        this.invocationContainer = new InvocationContainerImpl(settings);
    }

    @Override
    public Object handle(Invocation invocation) throws Throwable {
        InvocationMatcher invocationMatcher = this.getInvocationMatcher(invocation);
        if (answerValue != null) {
            this.saveStubbing(invocationMatcher);
        } else if (verificationMode != null) {
            try {
                this.verifyForInvocation(verificationMode, invocationMatcher);
            } finally {
                verificationMode = null;
            }
        } else {
            // It's a regular invocation. Try to see if we have a stub for it
            StubbedInvocationMatcher answer = invocationContainer.findAnswerFor(invocation);
            invocationContainer.setInvocationForPotentialStubbing(invocationMatcher);

            if (answer != null) {
                return answer.answer(invocation);
            }
        }

        // At this point the method was not stubbed. Make sure to return primitive values, to prevent
        // NPE due to Java auto-unboxing
        Class<?> type = invocation.getMethod().getReturnType();

        if (Primitives.isPrimitiveOrWrapper(type)) {
            return Primitives.defaultValue(type);
        }

        return null;
    }

    private void verifyForInvocation(VerificationMode verificationMode, InvocationMatcher invocationMatcher) {
        VerificationData data = new VerificationDataImpl(invocationContainer, invocationMatcher);
        verificationMode.verify(data);
    }

    private void saveStubbing(InvocationMatcher invocationMatcher) {
        invocationContainer.setAnswersForStubbing(Collections.singletonList(answerValue));
        invocationContainer.setMethodForStubbing(invocationMatcher);

        answerValue = null;
    }

    private InvocationMatcher getInvocationMatcher(Invocation invocation) {
        return matchersBinder.bindMatchers(
            mockingProgress().getArgumentMatcherStorage(),
            invocation
        );
    }

    @Override
    public MockCreationSettings<T> getMockSettings() {
        return settings;
    }

    @Override
    public InvocationContainer getInvocationContainer() {
        return invocationContainer;
    }
}
