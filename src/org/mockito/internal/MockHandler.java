/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.*;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;

import java.util.List;

/**
 * Invocation handler set on mock objects.
 * 
 * @param <T>
 *            type of mock object to handle
 */
public class MockHandler<T> implements MockitoInvocationHandler, MockHandlerInterface<T> {

    private static final long serialVersionUID = -2917871070982574165L;

    MockitoStubber mockitoStubber;
    MatchersBinder matchersBinder = new MatchersBinder();
    MockingProgress mockingProgress = new ThreadSafeMockingProgress();

    private final MockSettingsImpl mockSettings;

    public MockHandler(MockSettingsImpl mockSettings) {
        this.mockSettings = mockSettings;
        this.mockingProgress = new ThreadSafeMockingProgress();
        this.matchersBinder = new MatchersBinder();
        this.mockitoStubber = new MockitoStubber(mockingProgress);
    }

    // for tests
    MockHandler() {
        this(new MockSettingsImpl());
    }

    public MockHandler(MockHandlerInterface<T> oldMockHandler) {
        this(oldMockHandler.getMockSettings());
    }

    public Object handle(Invocation invocation) throws Throwable {
        if (mockitoStubber.hasAnswersForStubbing()) {
            // stubbing voids with stubVoid() or doAnswer() style
            InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(mockingProgress
                            .getArgumentMatcherStorage(), invocation);
            mockitoStubber.setMethodForStubbing(invocationMatcher);
            return null;
        }
        VerificationMode verificationMode = mockingProgress.pullVerificationMode();

        InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(),
                        invocation);

        mockingProgress.validateState();

        if (verificationMode != null) {
            VerificationDataImpl data = new VerificationDataImpl(mockitoStubber.getInvocations(), invocationMatcher);
            verificationMode.verify(data);
            return null;
        }
        
        mockitoStubber.setInvocationForPotentialStubbing(invocationMatcher);
        OngoingStubbingImpl<T> ongoingStubbing = new OngoingStubbingImpl<T>(mockitoStubber);
        mockingProgress.reportOngoingStubbing(ongoingStubbing);

        StubbedInvocationMatcher stubbedInvocation = mockitoStubber.findAnswerFor(invocation);
        if (!invocation.isVoid() && stubbedInvocation == null) {
            // it is a return-value interaction but not stubbed. This *might* be
            // a problem
            mockingProgress.getDebuggingInfo().addPotentiallyUnstubbed(invocationMatcher);
        }

        if (stubbedInvocation != null) {
            mockingProgress.getDebuggingInfo().reportUsedStub(invocationMatcher);
            stubbedInvocation.captureArgumentsFrom(invocation);
            return stubbedInvocation.answer(invocation);
        } else {
            Object ret = mockSettings.getDefaultAnswer().answer(invocation);

            // redo setting invocation for potential stubbing in case of partial
            // mocks / spies.
            // Without it, the real method inside 'when' might have delegated
            // to other self method and overwrite the intended stubbed method
            // with a different one.
            mockitoStubber.resetInvocationForPotentialStubbing(invocationMatcher);
            return ret;
        }
    }

    public void verifyNoMoreInteractions() {
        VerificationDataImpl data = new VerificationDataImpl(mockitoStubber.getInvocations(), null);
        VerificationModeFactory.noMoreInteractions().verify(data);
    }

    public VoidMethodStubbable<T> voidMethodStubbable(T mock) {
        return new VoidMethodStubbableImpl<T>(mock, mockitoStubber);
    }

    public MockSettingsImpl getMockSettings() {
        return mockSettings;
    }

    @SuppressWarnings("unchecked")
    public void setAnswersForStubbing(List<Answer> answers) {
        mockitoStubber.setAnswersForStubbing(answers);
    }

    public InvocationContainer getInvocationContainer() {
        return mockitoStubber;
    }
}