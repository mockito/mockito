package org.mockito.internal;

import java.lang.reflect.Method;
import java.util.*;

import org.mockito.exceptions.*;
import org.mockito.internal.matchers.*;

public class MockControl<T> implements MockAwareInvocationHandler<T>, MockitoExpectation<T>, VoidMethodExpectation<T>, MethodSelector<T> {

    private final MockitoBehavior<T> behavior = new MockitoBehavior<T>();
    private final MockitoState mockitoState;
    private final LastArguments lastArguments;

    private Throwable throwableToBeSetOnVoidMethod;
    
    public MockControl(MockitoState mockitoState, LastArguments lastArguments) {
        this.mockitoState = mockitoState;
        this.lastArguments = lastArguments;
    }
    
    /**
     * if user passed bare arguments, not matchers then create EqualsMatchers for every argument
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

    private void validateMatchers(Invocation invocation, List<IArgumentMatcher> matchers) {
        if (matchers != null && matchers.size() != invocation.getArguments().length) {
            throw new InvalidUseOfMatchersException(
                    + invocation.getArguments().length
                    + " matchers expected, " + matchers.size()
                    + " recorded.");
        }
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        VerifyingMode verifyingMode = mockitoState.pullVerifyingMode();
        
        Invocation invocation = new Invocation(proxy, method, args);
        List<IArgumentMatcher> lastMatchers = lastArguments.pullMatchers();
        validateMatchers(invocation, lastMatchers);

        List<IArgumentMatcher> processedMatchers = createEqualsMatchers(invocation, lastMatchers);
        InvocationWithMatchers invocationWithMatchers = new InvocationWithMatchers(invocation, processedMatchers);
        
        if (verifyingMode != null) {
            behavior.verify(invocationWithMatchers, verifyingMode);
            return ToTypeMappings.emptyReturnValueFor(method.getReturnType());
        }
        
//        else if (mockitoState.mockStubbingScenario()) {
//            mockitoState.stubbingCompleted();
//        }
        
        mockitoState.reportLastControl(this);
        
        behavior.addInvocation(invocationWithMatchers);
        
        if (throwableToBeSetOnVoidMethod != null) {
            andThrows(throwableToBeSetOnVoidMethod);
            throwableToBeSetOnVoidMethod = null;
            return null;
        }

        return behavior.resultFor(invocation);
    }

    public void verifyNoMoreInteractions() {
        behavior.verifyNoMoreInteractions();
    }
    
    public void verifyZeroInteractions() {
        behavior.verifyZeroInteractions();
    }

    public void andReturn(T value) {
//      TODO count number of andReturn vs number of stubbing
        behavior.addResult(Result.createReturnResult(value));
    }

    public void andThrows(Throwable throwable) {
        validateThrowable(throwable);
        //TODO count number of andReturn vs number of stubbing
        behavior.addResult(Result.createThrowResult(throwable));
    }
    
    private void validateThrowable(Throwable throwable) {
        if (throwable == null) {
            throw new MockitoException("Cannot set null throwable");
        }

        if (throwable instanceof RuntimeException || throwable instanceof Error) {
            return;
        }
    
        if (!isValidCheckedException(throwable)) {
            throw new MockitoException("Given checked exception is invalid for this method"); 
        }
    }

    private boolean isValidCheckedException(Throwable throwable) {
        Invocation lastInvocation = behavior.lastInvocation();

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
        throwableToBeSetOnVoidMethod = throwable;
        return this;
    }

    public T on() {
        return (T) behavior.getMock();
    }

    public void setMock(T mock) {
        behavior.setMock(mock);
    }
}