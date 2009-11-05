/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.util.List;

import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.*;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.*;
import org.mockito.internal.util.MockName;
import org.mockito.internal.verification.*;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;

/**
 * Invocation handler set on mock objects.
 * 
 * @param <T>
 *            type of mock object to handle
 */
public class MockHandler<T> implements IMockHandler {

    private static final long serialVersionUID = -2917871070982574165L;

    MockitoStubber mockitoStubber;
    MatchersBinder matchersBinder;
    MockingProgress mockingProgress;

    private final RegisteredInvocations registeredInvocations;
    private final MockName mockName;
    private final MockSettingsImpl mockSettings;

    public MockHandler(MockName mockName, MockingProgress mockingProgress, MatchersBinder matchersBinder,
                    MockSettingsImpl mockSettings) {
        this.mockName = mockName;
        this.mockingProgress = mockingProgress;
        this.matchersBinder = matchersBinder;
        this.mockSettings = mockSettings;
        this.mockitoStubber = new MockitoStubber(mockingProgress);
        this.registeredInvocations = new RegisteredInvocations();
    }

    public MockHandler(MockHandler<T> oldMockHandler) {
        this(oldMockHandler.mockName, oldMockHandler.mockingProgress, oldMockHandler.matchersBinder,
                        oldMockHandler.mockSettings);
    }

    // for tests
    MockHandler() {
        this(new MockName("mockie for tests", MockHandler.class), new ThreadSafeMockingProgress(),
                        new MatchersBinder(), new MockSettingsImpl());
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
            VerificationDataImpl data = new VerificationDataImpl(registeredInvocations.getAll(), invocationMatcher);
            verificationMode.verify(data);
            return null;
        }

        registeredInvocations.add(invocationMatcher.getInvocation());
        mockitoStubber.setInvocationForPotentialStubbing(invocationMatcher);
        OngoingStubbingImpl<T> ongoingStubbing = new OngoingStubbingImpl<T>(mockitoStubber, registeredInvocations);
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
            mockitoStubber.setInvocationForPotentialStubbing(invocationMatcher);
            return ret;
        }
    }

    public void verifyNoMoreInteractions() {
        VerificationDataImpl data = new VerificationDataImpl(registeredInvocations.getAll(), null);
        VerificationModeFactory.noMoreInteractions().verify(data);
    }

    public VoidMethodStubbable<T> voidMethodStubbable(T mock) {
        return new VoidMethodStubbableImpl<T>(mock, mockitoStubber);
    }

    public List<Invocation> getRegisteredInvocations() {
        return registeredInvocations.getAll();
    }

    public MockName getMockName() {
        return mockName;
    }

    @SuppressWarnings("unchecked")
    public void setAnswersForStubbing(List<Answer> answers) {
        mockitoStubber.setAnswersForStubbing(answers);
    }
}