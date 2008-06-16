/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.mockito.exceptions.base.StackTraceFilter;
import org.mockito.internal.configuration.Configuration;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.MockingProgress;

@SuppressWarnings("unchecked")
public class Stubber {

    private final LinkedList<StubbedInvocationMatcher> stubbed = new LinkedList<StubbedInvocationMatcher>();
    private final MockingProgress mockingProgress;
    private final List<Throwable> throwablesForVoidMethod = new ArrayList<Throwable>();
    private final AnswerFactory answerFactory = new AnswerFactory(new StackTraceFilter());
    
    private InvocationMatcher invocationForStubbing;
    
    public Stubber(MockingProgress mockingProgress) {
        this.mockingProgress = mockingProgress;
    }

    public void setInvocationForPotentialStubbing(InvocationMatcher invocation) {
        this.invocationForStubbing = invocation;
    }
    
    public void addReturnValue(Object value) {
        mockingProgress.stubbingCompleted();
        Answer answer = answerFactory.createReturningAnswer(value);
        stubbed.addFirst(new StubbedInvocationMatcher(invocationForStubbing, answer));
    }
    
    public void addThrowable(Throwable throwable) {
        mockingProgress.stubbingCompleted();
        Answer answer = answerFactory.createThrowingAnswer(throwable, invocationForStubbing.getInvocation());
        stubbed.addFirst(new StubbedInvocationMatcher(invocationForStubbing, answer));
    }
    
    public void addConsecutiveReturnValue(Object value) {
        stubbed.getFirst().addAnswer(answerFactory.createReturningAnswer(value));
    }

    public void addConsecutiveThrowable(Throwable throwable) {
        stubbed.getFirst().addAnswer(answerFactory.createThrowingAnswer(throwable, invocationForStubbing.getInvocation()));
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
}