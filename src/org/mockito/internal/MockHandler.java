/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.util.List;

import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

/**
 * Invocation handler set on mock objects.
 * 
 * @param <T>
 *            type of mock object to handle
 */
public class MockHandler<T> implements MockitoInvocationHandler, MockHandlerInterface<T> {

    private static final long serialVersionUID = -2917871070982574165L;

    InvocationContainerImpl invocationContainerImpl;
    MatchersBinder matchersBinder = new MatchersBinder();
    MockingProgress mockingProgress = new ThreadSafeMockingProgress();

    private final MockSettingsImpl mockSettings;

    public MockHandler(MockSettingsImpl mockSettings) {
        this.mockSettings = mockSettings;
        this.mockingProgress = new ThreadSafeMockingProgress();
        this.matchersBinder = new MatchersBinder();
        this.invocationContainerImpl = new InvocationContainerImpl(mockingProgress);
    }

    // for tests
    MockHandler() {
        this(new MockSettingsImpl());
    }

    public MockHandler(MockHandlerInterface<T> oldMockHandler) {
        this(oldMockHandler.getMockSettings());
    }

    public Object handle(Invocation invocation) throws Throwable {
        if (invocationContainerImpl.hasAnswersForStubbing()) {
            // stubbing voids with stubVoid() or doAnswer() style
            InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(mockingProgress
                            .getArgumentMatcherStorage(), invocation);
            invocationContainerImpl.setMethodForStubbing(invocationMatcher);
            return null;
        }
        VerificationMode verificationMode = mockingProgress.pullVerificationMode();

        InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(),
                        invocation);

        mockingProgress.validateState();

        //if verificationMode is not null then someone is doing verify()        
        if (verificationMode != null) {
            //We need to check if verification was started on the correct mock 
            // - see VerifyingWithAnExtraCallToADifferentMockTest (bug 138)
            //TODO: can I avoid this cast here?
            if (((MockAwareVerificationMode) verificationMode).getMock() == invocation.getMock()) {                
                VerificationDataImpl data = new VerificationDataImpl(invocationContainerImpl, invocationMatcher);            
                verificationMode.verify(data);
                return null;
            } else {
                // this means there is an invocation on a different mock. Re-adding verification mode 
                // - see VerifyingWithAnExtraCallToADifferentMockTest (bug 138)
                mockingProgress.verificationStarted(verificationMode);
            }
        }
        
        invocationContainerImpl.setInvocationForPotentialStubbing(invocationMatcher);
        OngoingStubbingImpl<T> ongoingStubbing = new OngoingStubbingImpl<T>(invocationContainerImpl);
        mockingProgress.reportOngoingStubbing(ongoingStubbing);

        StubbedInvocationMatcher stubbedInvocation = invocationContainerImpl.findAnswerFor(invocation);

        if (stubbedInvocation != null) {
            stubbedInvocation.captureArgumentsFrom(invocation);
            return stubbedInvocation.answer(invocation);
        } else {
            Object ret = mockSettings.getDefaultAnswer().answer(invocation);

            // redo setting invocation for potential stubbing in case of partial
            // mocks / spies.
            // Without it, the real method inside 'when' might have delegated
            // to other self method and overwrite the intended stubbed method
            // with a different one. The reset is required to avoid runtime exception that validates return type with stubbed method signature.
            invocationContainerImpl.resetInvocationForPotentialStubbing(invocationMatcher);
            return ret;
        }
    }

    public VoidMethodStubbable<T> voidMethodStubbable(T mock) {
        return new VoidMethodStubbableImpl<T>(mock, invocationContainerImpl);
    }

    public MockSettingsImpl getMockSettings() {
        return mockSettings;
    }

    @SuppressWarnings("unchecked")
    public void setAnswersForStubbing(List<Answer> answers) {
        invocationContainerImpl.setAnswersForStubbing(answers);
    }

    public InvocationContainer getInvocationContainer() {
        return invocationContainerImpl;
    }
}