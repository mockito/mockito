package org.mockito.internal;

import java.lang.reflect.*;
import java.util.List;

import org.mockito.internal.matchers.IArgumentMatcher;

public class MockitoControl<T> implements MockAwareInvocationHandler, InvocationHandler, MockitoExpectation<T>, VoidMethodExpectation<T>, MethodSelector<T> {

    private MockitoBehavior behavior = new MockitoBehavior();
    private Object mock;
    
    public MockitoControl() {}

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<IArgumentMatcher> lastMatchers = LastArguments.pullMatchers();

        MockitoInvocation invocation = new MockitoInvocation(proxy, method, args);
        InvocationWithMatchers invocationWithMatchers = new InvocationWithMatchers(invocation, lastMatchers);
        
        if (MockitoOperations.mockVerificationScenario()) {
            VerifyingMode verifyingMode = MockitoOperations.removeVerifyingMode();
            
            behavior.verify(invocationWithMatchers, verifyingMode);
            return ToTypeMappings.emptyReturnValueFor(method.getReturnType());
        }
        
        MockitoOperations.reportLastControlForStubbing(this);
        
        behavior.addInvocation(invocationWithMatchers);
        
        if (MockitoOperations.settingThrowableOnVoidMethodScenario()) {
            Throwable throwable = MockitoOperations.removeThrowableToBeSetOnVoidMethod();
        
            andThrows(throwable);
            return null;
        }

        return behavior.resultFor(invocation);
    }

    public void verifyNoMoreInteractions() {
        behavior.verifyNoMoreInteractions();
    }

    public void andReturn(T value) {
//      TODO count number of andReturn vs number of stubbing
//      TODO why do we need that in easy_Mock: value = convertNumberClassIfNeccessary(value);
        behavior.addResult(Result.createReturnResult(value));
    }

    public void andThrows(Throwable throwable) {
        validateThrowable(throwable);
        //TODO count number of andReturn vs number of stubbing
        behavior.addResult(Result.createThrowResult(throwable));
    }
    
    private void validateThrowable(Throwable throwable) {
        if (throwable == null) {
            throw new IllegalArgumentException("Cannot set null throwable");
        }

        if (throwable instanceof RuntimeException || throwable instanceof Error) {
            return;
        }
    
        if (! isValidCheckedException(throwable)) {
            throw new IllegalArgumentException("Given checked exception is invalid for this method"); 
        }
    }

    private boolean isValidCheckedException(Throwable throwable) {
        MockitoInvocation lastInvocation = behavior.lastInvocation();

        Class<?>[] exceptions = lastInvocation.getMethod().getExceptionTypes();
        Class<?> throwableClass = throwable.getClass();
        for (Class<?> exception : exceptions) {
            if (exception.isAssignableFrom(throwableClass)) {
                return true;
            }
        }
        
        return false;
    }

    public MethodSelector<T> toThrow(Throwable throwable) {
        //TODO refactor so we don't use static state to keep the throwable
        MockitoOperations.reportThrowableToBeSetOnVoidMethod(throwable);
        return this;
    }

    public T on() {
        // TODO check if mock can be of the type T
        return (T) mock;
    }

    public void setMock(Object mock) {
        this.mock = mock;
    }
}