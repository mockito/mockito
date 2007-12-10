/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.lang.reflect.Method;
import java.util.List;

public class MockControl<T> implements MockAwareInvocationHandler<T>, MockitoExpectation<T>, VoidMethodExpectation<T>, MethodSelector<T> {

    private final MockitoBehavior<T> behavior = new MockitoBehavior<T>();
    private final Stubber stubber = new Stubber();
    private final InvocationMatcherFactory invocationMatcherFactory = new InvocationMatcherFactory();
    
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (stubber.hasThrowableForVoidMethod()) {
            InvocationMatcher invocationMatcher = invocationMatcherFactory.create(proxy, method, args);
            stubber.addVoidMethodForThrowable(invocationMatcher);
            return null;
        }
        
        VerifyingMode verifyingMode = MockitoState.instance().pullVerifyingMode();
        MockitoState.instance().validateState();
        
        InvocationMatcher invocationWithMatchers = invocationMatcherFactory.create(proxy, method, args);
        
        if (verifyingMode != null) {
            behavior.verify(invocationWithMatchers, verifyingMode);
            return EmptyReturnValues.emptyValueFor(method.getReturnType());
        } 
        
        stubber.setInvocationForPotentialStubbing(invocationWithMatchers);
        behavior.addInvocation(invocationWithMatchers);

        MockitoState.instance().reportControlForStubbing(this);
        
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