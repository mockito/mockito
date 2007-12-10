/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.lang.reflect.Method;
import java.util.List;

public class MockControl<T> implements MockAwareInvocationHandler<T>, MockitoExpectation<T>, VoidMethodExpectation<T>, MethodSelector<T> {

    private final MockitoBehavior<T> behavior = new MockitoBehavior<T>();
    private final Stubber stubber;
    private final MatchersBinder matchersBinder;
    private final MockitoState mockitoState;
    
    public MockControl(MockitoState mockitoState, MatchersBinder matchersBinder) {
        this.mockitoState = mockitoState;
        this.matchersBinder = matchersBinder;
        stubber = new Stubber(mockitoState);
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
        InvocationMatcher invocationWithMatchers = matchersBinder.bindMatchers(invocation);
        
        if (verifyingMode != null) {
            behavior.verify(invocationWithMatchers, verifyingMode);
            return EmptyReturnValues.emptyValueFor(method.getReturnType());
        } 
        
        stubber.setInvocationForPotentialStubbing(invocationWithMatchers);
        behavior.addInvocation(invocationWithMatchers);

        mockitoState.reportControlForStubbing(this);
        
        return stubber.resultFor(invocationWithMatchers.getInvocation());
    }

    public void verifyNoMoreInteractions() {
        behavior.verifyNoMoreInteractions();
    }
    
    public void verifyZeroInteractions() {
        behavior.verifyZeroInteractions();
    }

    public void andReturn(T value) {
        behavior.lastInvocationWasStubbed();
        stubber.addReturnValue(value);
    }

    public void andThrows(Throwable throwable) {
        behavior.lastInvocationWasStubbed();
        stubber.addThrowable(throwable);
    }
    
    public MethodSelector<T> toThrow(Throwable throwable) {
        stubber.addThrowableForVoidMethod(throwable);
        return this;
    }

    public T on() {
        return (T) behavior.getMock();
    }

    public void setMock(T mock) {
        behavior.setMock(mock);
    }

    public List<Invocation> getRegisteredInvocations() {
        return behavior.getRegisteredInvocations();
    }
}