package org.mockito.internal;

import java.lang.reflect.*;
import java.util.*;

import org.mockito.exceptions.InvalidUseOfMatchersException;
import org.mockito.internal.matchers.*;

public class MockitoControl<T> implements MockAwareInvocationHandler<T>, InvocationHandler, MockitoExpectation<T>, VoidMethodExpectation<T>, MethodSelector<T> {

    private MockitoBehavior behavior = new MockitoBehavior();
    private T mock;
    private final MockitoState mockitoState;
    private final LastArguments lastArguments;
    
    public MockitoControl(MockitoState mockitoState, LastArguments lastArguments) {
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
        VerifyingMode verifyingMode = mockitoState.removeVerifyingMode();
        
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
        
        if (mockitoState.settingThrowableOnVoidMethodScenario()) {
            Throwable throwable = mockitoState.removeThrowableToBeSetOnVoidMethod();
        
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
        //TODO refactor so we don't use static state to keep the throwable
        mockitoState.reportThrowableToBeSetOnVoidMethod(throwable);
        return this;
    }

    public T on() {
        return (T) mock;
    }

    public void setMock(T mock) {
        this.mock = mock;
    }
}