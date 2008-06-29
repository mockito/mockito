/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.configuration.Configuration;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.MockingProgress;

@SuppressWarnings("unchecked")
public class Stubber {

    private final LinkedList<StubbedInvocationMatcher> stubbed = new LinkedList<StubbedInvocationMatcher>();
    private final MockingProgress mockingProgress;
    private final List<Answer> answersForVoidMethod = new ArrayList<Answer>();

    private InvocationMatcher invocationForStubbing;

    public Stubber(MockingProgress mockingProgress) {
        this.mockingProgress = mockingProgress;
    }

    public void setInvocationForPotentialStubbing(InvocationMatcher invocation) {
        this.invocationForStubbing = invocation;
    }

    public void addAnswer(Answer answer) {
        addAnswer(answer, false);
    }

    public void addConsecutiveAnswer(Answer answer) {
        addAnswer(answer, true);
    }
    
    private void addAnswer(Answer answer, boolean isConsecutive) {
        mockingProgress.stubbingCompleted();
        if (answer instanceof ThrowsException) {
            new ExceptionsValidator().validate(((ThrowsException) answer).getThrowable(), invocationForStubbing.getInvocation());
        }
        
        if (isConsecutive) {
            stubbed.getFirst().addAnswer(answer);
        } else {
            stubbed.addFirst(new StubbedInvocationMatcher(invocationForStubbing, answer));
        }
    }    

    public Object resultFor(Invocation invocation) throws Throwable {
        for (StubbedInvocationMatcher s : stubbed) {
            if (s.matches(invocation)) {
                return s.answer(invocation);
            }
        }
        return Configuration.instance().getReturnValues().valueFor(invocation);
    }

    public void addAnswerForVoidMethod(Answer answer) {
        answersForVoidMethod.add(answer);
    }

    public boolean hasAnswerForVoidMethod() {
        return !answersForVoidMethod.isEmpty();
    }

    public void addVoidMethodForStubbing(InvocationMatcher voidMethodInvocationMatcher) {
        invocationForStubbing = voidMethodInvocationMatcher;
        assert hasAnswerForVoidMethod();
        for (int i = 0; i < answersForVoidMethod.size(); i++) {
            addAnswer(answersForVoidMethod.get(i), i != 0);
        }
        answersForVoidMethod.clear();
    }
}