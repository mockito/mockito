/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import net.sf.cglib.proxy.MethodProxy;

import org.mockito.internal.configuration.Configuration;
import org.mockito.internal.creation.MockAwareInterceptor;
import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.OngoingStubbing;
import org.mockito.internal.progress.VerificationModeImpl;
import org.mockito.internal.stubbing.MockitoStubber;
import org.mockito.internal.stubbing.Returns;
import org.mockito.internal.stubbing.DoesNothing;
import org.mockito.internal.stubbing.ThrowsException;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.MissingInvocationInOrderVerifier;
import org.mockito.internal.verification.MissingInvocationVerifier;
import org.mockito.internal.verification.NoMoreInvocationsVerifier;
import org.mockito.internal.verification.NumberOfInvocationsInOrderVerifier;
import org.mockito.internal.verification.NumberOfInvocationsVerifier;
import org.mockito.internal.verification.Verifier;
import org.mockito.internal.verification.VerifyingRecorder;
import org.mockito.stubbing.Answer;

/**
 * Invocation handler set on mock objects.
 *
 * @param <T> type of mock object to handle
 */
public class MockHandler<T> implements MockAwareInterceptor<T> {

    private final VerifyingRecorder verifyingRecorder;
    private final MockitoStubber mockitoStubber;
    private final MatchersBinder matchersBinder;
    private final MockingProgress mockingProgress;
    private final String mockName;

    private T instance;

    public MockHandler(String mockName, MockingProgress mockingProgress, MatchersBinder matchersBinder) {
        this.mockName = mockName;
        this.mockingProgress = mockingProgress;
        this.matchersBinder = matchersBinder;
        this.mockitoStubber = new MockitoStubber(mockingProgress);

        verifyingRecorder = createRecorder();
    }
    
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if (mockitoStubber.hasAnswersForStubbing()) {
            //stubbing voids in the old-school way: stubVoid()
            Invocation invocation = new Invocation(proxy, method, args, mockingProgress.nextSequenceNumber());
            InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(invocation);
            mockitoStubber.setMethodForStubbing(invocationMatcher);
            return null;
        }
        
        VerificationModeImpl verificationMode = mockingProgress.pullVerificationMode();
        mockingProgress.validateState();

        Invocation invocation = new Invocation(proxy, method, args, mockingProgress.nextSequenceNumber());
        InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(invocation);

        if (verificationMode != null) {
            //verifying
            verifyingRecorder.verify(invocationMatcher, verificationMode);
            return null;
        }

        mockitoStubber.setInvocationForPotentialStubbing(invocationMatcher);
        verifyingRecorder.recordInvocation(invocationMatcher.getInvocation());

        mockingProgress.reportOngoingStubbing(new OngoingStubbingImpl());

        if (mockitoStubber.hasResultFor(invocation)) {
            return mockitoStubber.getResultFor(invocation);
        } else if (MockUtil.isMock(instance)) {
            return Configuration.instance().getReturnValues().valueFor(invocation);
        } else {
            return methodProxy.invoke(instance, args);
        }
    }

    public void verifyNoMoreInteractions() {
        verifyingRecorder.verify(VerificationModeImpl.noMoreInteractions());
    }

    public VoidMethodStubbable<T> voidMethodStubbable(T mock) {
        return new VoidMethodStubbableImpl(mock);
    }

    public void setInstance(T instance) {
        this.instance = instance;
    }

    public List<Invocation> getRegisteredInvocations() {
        return verifyingRecorder.getRegisteredInvocations();
    }

    public String getMockName() {
        return mockName;
    }

    private VerifyingRecorder createRecorder() {
        List<Verifier> verifiers = Arrays.asList(
                new MissingInvocationInOrderVerifier(),
                new NumberOfInvocationsInOrderVerifier(),
                new MissingInvocationVerifier(),
                new NumberOfInvocationsVerifier(),
                new NoMoreInvocationsVerifier());
        return new VerifyingRecorder(new AllInvocationsFinder(), verifiers);
    }

    private final class VoidMethodStubbableImpl implements VoidMethodStubbable<T> {
        private final T mock;

        public VoidMethodStubbableImpl(T mock) {
            this.mock = mock;
        }

        public VoidMethodStubbable<T> toThrow(Throwable throwable) {
            mockitoStubber.addAnswerForVoidMethod(new ThrowsException(throwable));
            return this;
        }

        public VoidMethodStubbable<T> toReturn() {
            mockitoStubber.addAnswerForVoidMethod(new DoesNothing());
            return this;
        }

        public VoidMethodStubbable<T> toAnswer(Answer<?> answer) {
            mockitoStubber.addAnswerForVoidMethod(answer);
            return this;
        }

        public T on() {
            return mock;
        }
    }

    private class OngoingStubbingImpl implements OngoingStubbing<T> {
        public OngoingStubbing<T> toReturn(Object value) {
            verifyingRecorder.eraseLastInvocation();
            mockitoStubber.addAnswer(new Returns(value));
            return new ConsecutiveStubbing();
        }

        public OngoingStubbing<T> toThrow(Throwable throwable) {
            verifyingRecorder.eraseLastInvocation();
            mockitoStubber.addAnswer(new ThrowsException(throwable));
            return new ConsecutiveStubbing();
        }

        public OngoingStubbing<T> toAnswer(Answer<?> answer) {
            verifyingRecorder.eraseLastInvocation();
            mockitoStubber.addAnswer(answer);
            return new ConsecutiveStubbing();
        }
    }

    private class ConsecutiveStubbing implements OngoingStubbing<T> {
        public OngoingStubbing<T> toReturn(Object value) {
            mockitoStubber.addConsecutiveAnswer(new Returns(value));
            return this;
        }

        public OngoingStubbing<T> toThrow(Throwable throwable) {
            mockitoStubber.addConsecutiveAnswer(new ThrowsException(throwable));
            return this;
        }

        public OngoingStubbing<T> toAnswer(Answer<?> answer) {
            mockitoStubber.addConsecutiveAnswer(answer);
            return this;
        }
    }

    @SuppressWarnings("unchecked")
    public void setAnswersForStubbing(List<Answer> answers) {
        mockitoStubber.setAnswersForStubbing(answers);
    }
}