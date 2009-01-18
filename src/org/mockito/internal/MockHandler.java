/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.lang.reflect.Method;
import java.util.List;

import net.sf.cglib.proxy.MethodProxy;

import org.mockito.ReturnValues;
import org.mockito.internal.creation.MockAwareInterceptor;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.DeprecatedOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.NewOngoingStubbing;
import org.mockito.internal.progress.SequenceNumber;
import org.mockito.internal.stubbing.DoesNothing;
import org.mockito.internal.stubbing.MockitoStubber;
import org.mockito.internal.stubbing.Returns;
import org.mockito.internal.stubbing.ThrowsException;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.RegisteredInvocations;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.stubbing.Answer;

/**
 * Invocation handler set on mock objects.
 *
 * @param <T> type of mock object to handle
 */
public class MockHandler<T> implements MockAwareInterceptor<T> {

    private final RegisteredInvocations registeredInvocations;
    private final MockitoStubber mockitoStubber;
    private final MatchersBinder matchersBinder;
    private final MockingProgress mockingProgress;
    private final MockName mockName;
    private final ReturnValues returnValues;

    private T instance;

    public MockHandler(MockName mockName, MockingProgress mockingProgress, MatchersBinder matchersBinder, ReturnValues returnValues) {
        this.mockName = mockName;
        this.mockingProgress = mockingProgress;
        this.matchersBinder = matchersBinder;
        this.returnValues = returnValues;
        this.mockitoStubber = new MockitoStubber(mockingProgress);
        this.registeredInvocations = new RegisteredInvocations();
    }
    
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if (mockitoStubber.hasAnswersForStubbing()) {
            //stubbing voids with stubVoid() or doAnswer() style
            Invocation invocation = new Invocation(proxy, method, args, SequenceNumber.next());
            InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation);
            mockitoStubber.setMethodForStubbing(invocationMatcher);
            return null;
        }
        VerificationMode verificationMode = mockingProgress.pullVerificationMode();

        Invocation invocation = new Invocation(proxy, method, args, SequenceNumber.next());
        InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation);
        
        mockingProgress.validateState();

        if (verificationMode != null) {
            VerificationDataImpl data = new VerificationDataImpl(registeredInvocations.getAll(), invocationMatcher);
            verificationMode.verify(data);
            return null;
        }

        mockitoStubber.setInvocationForPotentialStubbing(invocationMatcher);
        registeredInvocations.add(invocationMatcher.getInvocation());

        mockingProgress.reportOngoingStubbing(new OngoingStubbingImpl());

        Answer<?> answer = mockitoStubber.findAnswerFor(invocation);
        if (!invocation.isVoid() && answer == null) {
            //it is a return-value interaction but not stubbed. This *might* be a problem
            mockingProgress.getDebuggingInfo().addPotentiallyUnstubbed(invocationMatcher);
        }
        
        if (answer != null) {
            mockingProgress.getDebuggingInfo().reportUsedStub(invocationMatcher);
            return answer.answer(invocation);
        } else if (MockUtil.isMock(instance)) {
            return returnValues.valueFor(invocation);
        } else {
            return methodProxy.invoke(instance, args);
        }
    }

    public void verifyNoMoreInteractions() {
        VerificationDataImpl data = new VerificationDataImpl(registeredInvocations.getAll(), null);
        VerificationModeFactory.noMoreInteractions().verify(data);
    }

    public VoidMethodStubbable<T> voidMethodStubbable(T mock) {
        return new VoidMethodStubbableImpl(mock);
    }

    public void setInstance(T instance) {
        this.instance = instance;
    }

    public List<Invocation> getRegisteredInvocations() {
        return registeredInvocations.getAll();
    }

    public MockName getMockName() {
        return mockName;
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

    private abstract class BaseStubbing implements NewOngoingStubbing<T>, DeprecatedOngoingStubbing<T> {
        public NewOngoingStubbing<T> thenReturn(T value) {
            return thenAnswer(new Returns(value));
        }

        public NewOngoingStubbing<T> thenReturn(T value, T... values) {
            NewOngoingStubbing<T> stubbing = thenReturn(value);            
            if (values == null) {
                return stubbing.thenReturn(null);
            }
            for (T v: values) {
                stubbing = stubbing.thenReturn(v);
            }
            return stubbing;
        }

        private NewOngoingStubbing<T> thenThrow(Throwable throwable) {
            return thenAnswer(new ThrowsException(throwable));
        }

        public NewOngoingStubbing<T> thenThrow(Throwable... throwables) {
            if (throwables == null) {
                thenThrow((Throwable) null);
            }
            NewOngoingStubbing<T> stubbing = null;
            for (Throwable t: throwables) {
                if (stubbing == null) {
                    stubbing = thenThrow(t);                    
                } else {
                    stubbing = stubbing.thenThrow(t);
                }
            }
            return stubbing;
        }        

        public DeprecatedOngoingStubbing<T> toReturn(T value) {
            return toAnswer(new Returns(value));
        }

        public DeprecatedOngoingStubbing<T> toThrow(Throwable throwable) {
            return toAnswer(new ThrowsException(throwable));
        }
    }
    
    private class OngoingStubbingImpl extends BaseStubbing {
        public NewOngoingStubbing<T> thenAnswer(Answer<?> answer) {
            registeredInvocations.removeLast();
            mockitoStubber.addAnswer(answer);
            return new ConsecutiveStubbing();
        }

        public DeprecatedOngoingStubbing<T> toAnswer(Answer<?> answer) {
            registeredInvocations.removeLast();
            mockitoStubber.addAnswer(answer);
            return new ConsecutiveStubbing();
        }
    }

    private class ConsecutiveStubbing extends BaseStubbing {
        public NewOngoingStubbing<T> thenAnswer(Answer<?> answer) {
            mockitoStubber.addConsecutiveAnswer(answer);
            return this;
        }
        
        public DeprecatedOngoingStubbing<T> toAnswer(Answer<?> answer) {
            mockitoStubber.addConsecutiveAnswer(answer);
            return this;
        }
    }    
    
    @SuppressWarnings("unchecked")
    public void setAnswersForStubbing(List<Answer> answers) {
        mockitoStubber.setAnswersForStubbing(answers);
    }

    public boolean hasName() {
        return false;
    }
}