/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.lang.reflect.Method;
import java.util.*;

import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.internal.matchers.*;

public class MockControl<T> implements MockAwareInvocationHandler<T>, MockitoExpectation<T>, VoidMethodExpectation<T>, MethodSelector<T> {

    private final MockitoBehavior<T> behavior = new MockitoBehavior<T>();
    private final Stubber stubber = new Stubber();
    private Throwable throwableToBeSetOnVoidMethod;
    
    /**
     * if user passed bare arguments then create EqualsMatcher for every argument
     */
    private List<IArgumentMatcher> createEqualsMatchers(Invocation invocation,
            List<IArgumentMatcher> matchers) {
        if (matchers != null) {
            return matchers;
        }
        List<IArgumentMatcher> result = new ArrayList<IArgumentMatcher>();
        for (Object argument : invocation.getArguments()) {
            result.add(new Equals(argument));
        }
        return result;
    }

    private void validateMatchers(Invocation invocation, List<IArgumentMatcher> matchers) throws InvalidUseOfMatchersException {
        if (matchers != null) {
            if (matchers.size() != invocation.getArguments().length) {
                throw new InvalidUseOfMatchersException(
                        + invocation.getArguments().length
                        + " matchers expected, " + matchers.size()
                        + " recorded.");
            }
        }
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (throwableToBeSetOnVoidMethod != null) {
            ExpectedInvocation invocationWithMatchers = expectedInvocation(proxy, method, args);
            //TODO this is a bit dodgy, we should set result directly on behavior and behavior should validate exception
            behavior.addInvocation(invocationWithMatchers);
            stubber.setInvocationForPotentialStubbing(invocationWithMatchers);
            andThrows(throwableToBeSetOnVoidMethod);
            throwableToBeSetOnVoidMethod = null;
            return null;
        }
        
        VerifyingMode verifyingMode = MockitoState.instance().pullVerifyingMode();
        MockitoState.instance().validateState();
        
        ExpectedInvocation invocationWithMatchers = expectedInvocation(proxy, method, args);
        
        if (verifyingMode != null) {
            behavior.verify(invocationWithMatchers, verifyingMode);
            return ToTypeMappings.emptyReturnValueFor(method.getReturnType());
        } 
        
        stubber.setInvocationForPotentialStubbing(invocationWithMatchers);
        behavior.addInvocation(invocationWithMatchers);

        MockitoState.instance().reportControlForStubbing(this);
        
        return stubber.resultFor(invocationWithMatchers.getInvocation());
    }

    private ExpectedInvocation expectedInvocation(Object proxy, Method method, Object[] args) {
        Invocation invocation = new Invocation(proxy, method, args, MockitoState.instance().nextSequenceNumber());
        
        List<IArgumentMatcher> lastMatchers = LastArguments.instance().pullMatchers();
        validateMatchers(invocation, lastMatchers);

        List<IArgumentMatcher> processedMatchers = createEqualsMatchers(invocation, lastMatchers);
        
        ExpectedInvocation invocationWithMatchers = new ExpectedInvocation(invocation, processedMatchers);
        return invocationWithMatchers;
    }

    public void verifyNoMoreInteractions() {
        behavior.verifyNoMoreInteractions();
    }
    
    public void verifyZeroInteractions() {
        behavior.verifyZeroInteractions();
    }

    public void andReturn(T value) {
        MockitoState.instance().stubbingCompleted();
        behavior.lastInvocationWasStubbed();
        stubber.addReturnValue(value);
    }

    public void andThrows(Throwable throwable) {
        MockitoState.instance().stubbingCompleted();
        behavior.lastInvocationWasStubbed();
        stubber.addThrowable(throwable);
    }
    
    public MethodSelector<T> toThrow(Throwable throwable) {
        throwableToBeSetOnVoidMethod = throwable;
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