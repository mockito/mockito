/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.StackTraceFilter;
import org.mockito.internal.configuration.Configuration;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.MockingProgress;

@SuppressWarnings("unchecked")
public class Stubber {

    private final LinkedList<StubbedInvocationMatcher> stubbed = new LinkedList<StubbedInvocationMatcher>();
    private final Reporter reporter = new Reporter();
    private final MockingProgress mockingProgress;
    private final List<Throwable> throwablesForVoidMethod = new ArrayList<Throwable>();
    
    private InvocationMatcher invocationForStubbing;
    
    public Stubber(MockingProgress mockingProgress) {
        this.mockingProgress = mockingProgress;
    }

    public void setInvocationForPotentialStubbing(InvocationMatcher invocation) {
        this.invocationForStubbing = invocation;
    }
    
    public void addReturnValue(Object value) {
        mockingProgress.stubbingCompleted();
        Answer answer = AnswerFactory.createReturningAnswer(value);
        stubbed.addFirst(new StubbedInvocationMatcher(invocationForStubbing, answer));
    }
    
    public void addThrowable(Throwable throwable) {
        mockingProgress.stubbingCompleted();
        validateThrowable(throwable);
        Answer answer = AnswerFactory.createThrowingAnswer(throwable, new StackTraceFilter());
        stubbed.addFirst(new StubbedInvocationMatcher(invocationForStubbing, answer));
    }
    
    public void addConsecutiveReturnValue(Object value) {
        stubbed.getFirst().addAnswer(AnswerFactory.createReturningAnswer(value));
    }

    public void addConsecutiveThrowable(Throwable throwable) {
        //TODO move validation of throwable to createThrowResult
        validateThrowable(throwable);
        stubbed.getFirst().addAnswer(AnswerFactory.createThrowingAnswer(throwable, new StackTraceFilter()));
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
        throwablesForVoidMethod.add(throwable);
    }

    public boolean hasThrowableForVoidMethod() {
        return !throwablesForVoidMethod.isEmpty();
    }
    
    public void addVoidMethodForThrowable(InvocationMatcher voidMethodInvocationMatcher) {
        invocationForStubbing = voidMethodInvocationMatcher;
        assert hasThrowableForVoidMethod();
        for (int i = 0; i < throwablesForVoidMethod.size(); i++) {
            Throwable throwable = throwablesForVoidMethod.get(i);
            if (i == 0) {
                addThrowable(throwable);
            } else {
                addConsecutiveThrowable(throwable);
            }
        }
        throwablesForVoidMethod.clear();
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