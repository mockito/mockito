/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.lang.reflect.Method;
import java.util.List;

public class MockControl<T> implements MockAwareInvocationHandler<T>, MockitoExpectation<T>, VoidMethodExpectation<T>, MethodSelector<T> {

    private final VerifyingRecorder<T> verifyingRecorder;
    private final Stubber stubber;
    private final MatchersBinder matchersBinder;
    private final MockitoState mockitoState;
    
    private T mock;
    
    public MockControl(MockitoState mockitoState, MatchersBinder matchersBinder) {
        this.mockitoState = mockitoState;
        this.matchersBinder = matchersBinder;
        stubber = new Stubber(mockitoState);
        verifyingRecorder = new VerifyingRecorder<T>();
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (stubber.hasThrowableForVoidMethod()) {
            Invocation invocation = new Invocation(proxy, method, args, mockitoState.nextSequenceNumber());
            InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(invocation);
            stubber.addVoidMethodForThrowable(invocationMatcher);
            return null;
        }
        
        VerifyingMode verifyingMode = mockitoState.pullVerifyingMode();
        mockitoState.validateState();
        
        Invocation invocation = new Invocation(proxy, method, args, mockitoState.nextSequenceNumber());
        InvocationMatcher invocationMatcher = matchersBinder.bindMatchers(invocation);
        
        if (verifyingMode != null) {
            verifyingRecorder.verify(invocationMatcher, verifyingMode);
            return EmptyReturnValues.emptyValueFor(method.getReturnType());
        } 
        
        stubber.setInvocationForPotentialStubbing(invocationMatcher);
        verifyingRecorder.recordInvocation(invocationMatcher);

        mockitoState.reportControlForStubbing(this);
        
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

    public void andThrows(Throwable throwable) {
        verifyingRecorder.eraseLastInvocation();
        stubber.addThrowable(throwable);
    }
    
    public MethodSelector<T> toThrow(Throwable throwable) {
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