/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.lang.reflect.Method;
import java.util.List;

import org.mockito.internal.creation.MockAwareInvocationHandler;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.state.MockitoState;
import org.mockito.internal.state.OngoingStubbing;
import org.mockito.internal.state.OngoingVerifyingMode;
import org.mockito.internal.stubbing.EmptyReturnValues;
import org.mockito.internal.stubbing.StubbedMethodSelector;
import org.mockito.internal.stubbing.Stubber;
import org.mockito.internal.stubbing.VoidMethodStubable;
import org.mockito.internal.verification.VerifyingRecorder;

public class MockControl<T> implements MockAwareInvocationHandler<T>, OngoingStubbing<T>, VoidMethodStubable<T>, StubbedMethodSelector<T> {

    private final VerifyingRecorder<T> verifyingRecorder;
    private final Stubber stubber;
    private final MatchersBinder matchersBinder;
    private final MockitoState mockitoState;
    
    private T mock;
    
    public MockControl(MockitoState mockitoState, MatchersBinder matchersBinder) {
        this.mockitoState = mockitoState;
        this.matchersBinder = matchersBinder;
        stubber = new Stubber(mockitoState);
        verifyingRecorder = new VerifyingRecorder<T>(new AllInvocationsFinder());
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (stubber.hasThrowableForVoidMethod()) {
            Invocation invocation = new Invocation(proxy, method, args, mockitoState.nextSequenceNumber());
            InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(invocation);
            stubber.addVoidMethodForThrowable(invocationMatcher);
            return null;
        }
        
        OngoingVerifyingMode ongoingVerifyingMode = mockitoState.pullVerifyingMode();
        mockitoState.validateState();
        
        Invocation invocation = new Invocation(proxy, method, args, mockitoState.nextSequenceNumber());
        InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(invocation);
        
        if (ongoingVerifyingMode != null) {
            verifyingRecorder.verify(invocationMatcher, ongoingVerifyingMode);
            return EmptyReturnValues.emptyValueFor(method.getReturnType());
        } 
        
        stubber.setInvocationForPotentialStubbing(invocationMatcher);
        verifyingRecorder.recordInvocation(invocationMatcher);

        mockitoState.reportStubable(this);
        
        return stubber.resultFor(invocationMatcher.getInvocation());
    }

    public void verifyNoMoreInteractions() {
        verifyingRecorder.verifyNoMoreInteractions();
    }
    
    public void verifyZeroInteractions() {
        verifyingRecorder.verifyZeroInteractions();
    }

    public void andReturn(T value) {
        verifyingRecorder.eraseLastInvocation();
        stubber.addReturnValue(value);
    }

    public void andThrow(Throwable throwable) {
        verifyingRecorder.eraseLastInvocation();
        stubber.addThrowable(throwable);
    }
    
    public StubbedMethodSelector<T> toThrow(Throwable throwable) {
        stubber.addThrowableForVoidMethod(throwable);
        return this;
    }

    public T on() {
        return mock;
    }

    public void setMock(T mock) {
        this.mock = mock;
    }

    public List<Invocation> getRegisteredInvocations() {
        return verifyingRecorder.getRegisteredInvocations();
    }
}