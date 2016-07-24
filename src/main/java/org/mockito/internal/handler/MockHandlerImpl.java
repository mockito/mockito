/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.handler;

import static org.mockito.internal.exceptions.Reporter.stubPassedToVerify;
import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

import java.util.List;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.invocation.Invocation;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

/**
 * Invocation handler set on mock objects.
 *
 * @param <T> type of mock object to handle
 */
class MockHandlerImpl<T> implements InternalMockHandler<T> {

    private static final long serialVersionUID = -2917871070982574165L;

    InvocationContainerImpl invocationContainerImpl;

    MatchersBinder matchersBinder = new MatchersBinder();

    private final MockCreationSettings<T> mockSettings;

    public MockHandlerImpl(MockCreationSettings<T> mockSettings) {
        this.mockSettings = mockSettings;
       
        this.matchersBinder = new MatchersBinder();
        this.invocationContainerImpl = new InvocationContainerImpl( mockSettings);
    }

    public Object handle(Invocation invocation) throws Throwable {
        if (invocationContainerImpl.hasAnswersForStubbing()) {
            // stubbing voids with doThrow() or doAnswer() style
            InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(
                    mockingProgress().getArgumentMatcherStorage(),
                    invocation
            );
            invocationContainerImpl.setMethodForStubbing(invocationMatcher);
            return null;
        }
        VerificationMode verificationMode = mockingProgress().pullVerificationMode();

        InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(
                mockingProgress().getArgumentMatcherStorage(),
                invocation
        );

        mockingProgress().validateState();

        // if verificationMode is not null then someone is doing verify()
        if (verificationMode != null) {
            // We need to check if verification was started on the correct mock
            // - see VerifyingWithAnExtraCallToADifferentMockTest (bug 138)
            if (((MockAwareVerificationMode) verificationMode).getMock() == invocation.getMock()) {
                VerificationDataImpl data = createVerificationData(invocationContainerImpl, invocationMatcher);
                verificationMode.verify(data);
                return null;
            } else {
                // this means there is an invocation on a different mock. Re-adding verification mode
                // - see VerifyingWithAnExtraCallToADifferentMockTest (bug 138)
                mockingProgress().verificationStarted(verificationMode);
            }
        }

        // prepare invocation for stubbing
        invocationContainerImpl.setInvocationForPotentialStubbing(invocationMatcher);
        OngoingStubbingImpl<T> ongoingStubbing = new OngoingStubbingImpl<T>(invocationContainerImpl);
        mockingProgress().reportOngoingStubbing(ongoingStubbing);

        // look for existing answer for this invocation
        StubbedInvocationMatcher stubbedInvocation = invocationContainerImpl.findAnswerFor(invocation);

        if (stubbedInvocation != null) {
            stubbedInvocation.captureArgumentsFrom(invocation);
            return stubbedInvocation.answer(invocation);
        } else {
            Object ret = mockSettings.getDefaultAnswer().answer(invocation);
            new AnswersValidator().validateDefaultAnswerReturnedValue(invocation, ret);

            // redo setting invocation for potential stubbing in case of partial
            // mocks / spies.
            // Without it, the real method inside 'when' might have delegated
            // to other self method and overwrite the intended stubbed method
            // with a different one. The reset is required to avoid runtime exception that validates return type with stubbed method signature.
            invocationContainerImpl.resetInvocationForPotentialStubbing(invocationMatcher);
            return ret;
        }
    }

    public MockCreationSettings<T> getMockSettings() {
        return mockSettings;
    }

    public void setAnswersForStubbing(List<Answer<?>> answers) {
        invocationContainerImpl.setAnswersForStubbing(answers);
    }

    public InvocationContainer getInvocationContainer() {
        return invocationContainerImpl;
    }

    private VerificationDataImpl createVerificationData(InvocationContainerImpl invocationContainerImpl, InvocationMatcher invocationMatcher) {
        if (mockSettings.isStubOnly()) {
            throw stubPassedToVerify();     // this throws an exception
        }

        return new VerificationDataImpl(invocationContainerImpl, invocationMatcher);
    }
}

