package org.mockito.internal;

import java.lang.reflect.*;
import java.util.*;

import org.mockito.exceptions.InvalidUseOfMatchersException;
import org.mockito.internal.matchers.*;

public class MockitoControl<T> implements MockAwareInvocationHandler, InvocationHandler, MockitoExpectation<T>, VoidMethodExpectation<T>, MethodSelector<T> {

    private MockitoBehavior behavior = new MockitoBehavior();
    private Object mock;
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
        MockitoInvocation invocation = new MockitoInvocation(proxy, method, args);
        List<IArgumentMatcher> lastMatchers = lastArguments.pullMatchers();
        List<IArgumentMatcher> processedMatchers = createEqualsMatchers(invocation, lastMatchers);
        InvocationWithMatchers invocationWithMatchers = new InvocationWithMatchers(invocation, processedMatchers);
        
        if (mockitoState.mockVerificationScenario()) {
            VerifyingMode verifyingMode = mockitoState.verifyingCompleted();

            //have to validate matcher after verifyingMode flag is cleared - a bit smelly
            validateMatchers(invocation, lastMatchers);
            
            behavior.verify(invocationWithMatchers, verifyingMode);
            return ToTypeMappings.emptyReturnValueFor(method.getReturnType());
        } else {
            validateMatchers(invocation, lastMatchers);
        }
        
//        else if (mockitoState.mockStubbingScenario()) {
//            mockitoState.stubbingCompleted();
//        }
        
        mockitoState.reportLastControlForStubbing(this);
        
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
        mockitoState.reportThrowableToBeSetOnVoidMethod(throwable);
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