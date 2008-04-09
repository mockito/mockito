/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import java.util.LinkedList;

import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.StackTraceFilter;
import org.mockito.internal.configuration.Configuration;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.MockingProgress;

public class Stubber {

    private final LinkedList<StubbedInvocationMatcher> stubbed = new LinkedList<StubbedInvocationMatcher>();
    private final Reporter reporter = new Reporter();
    private final MockingProgress mockingProgress;
    
    private InvocationMatcher invocationForStubbing;
    private Throwable throwableForVoidMethod;
    
    public Stubber(MockingProgress mockingProgress) {
        this.mockingProgress = mockingProgress;
    }

    public void setInvocationForPotentialStubbing(InvocationMatcher invocation) {
        this.invocationForStubbing = invocation;
    }
    
    public void addReturnValue(Object value) {
        mockingProgress.stubbingCompleted();
        addResult(Result.createReturnResult(value));
    }
    
    public void addThrowable(Throwable throwable) {
        mockingProgress.stubbingCompleted();
        validateThrowable(throwable);
        addResult(Result.createThrowResult(throwable, new StackTraceFilter()));
    }

    private void addResult(Result result) {
        assert invocationForStubbing != null;
        stubbed.addFirst(new StubbedInvocationMatcher(invocationForStubbing, result));
    }

    public Object resultFor(Invocation invocation) throws Throwable {
        for (StubbedInvocationMatcher s : stubbed) {
            if (s.matches(invocation)) {
                return s.answer();
            }
        }
        return Configuration.instance().getReturnValues().valueFor(invocation);
    }

    public void addThrowableForVoidMethod(Throwable throwable) {
        throwableForVoidMethod = throwable;
    }

    public boolean hasThrowableForVoidMethod() {
        return throwableForVoidMethod != null;
    }
    
    public void addVoidMethodForThrowable(InvocationMatcher voidMethodInvocationMatcher) {
        invocationForStubbing = voidMethodInvocationMatcher;
        addThrowable(throwableForVoidMethod);
        throwableForVoidMethod = null;
    }
    
    private void validateThrowable(Throwable throwable) {
        if (throwable == null) {
            reporter.cannotStubWithNullThrowable();
        }

        if (throwable instanceof RuntimeException || throwable instanceof Error) {
            return;
        }
    
        if (!isValidCheckedException(throwable)) {
            reporter.checkedExceptionInvalid(throwable);
        }
    }

    private boolean isValidCheckedException(Throwable throwable) {
        Invocation lastInvocation = invocationForStubbing.getInvocation();

        Class<?>[] exceptions = lastInvocation.getMethod().getExceptionTypes();
        Class<?> throwableClass = throwable.getClass();
        for (Class<?> exception : exceptions) {
            if (exception.isAssignableFrom(throwableClass)) {
                return true;
            }
        }
        
        return false;
    }
}
