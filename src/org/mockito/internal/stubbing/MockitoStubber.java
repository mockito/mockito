/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.stubbing.Answer;

@SuppressWarnings("unchecked")
public class MockitoStubber {

    private final LinkedList<StubbedInvocationMatcher> stubbed = new LinkedList<StubbedInvocationMatcher>();
    private final MockingProgress mockingProgress;
    private final List<Answer> answersForVoidMethod = new ArrayList<Answer>();

    private InvocationMatcher invocationForStubbing;

    public MockitoStubber(MockingProgress mockingProgress) {
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
        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.validate(answer, invocationForStubbing.getInvocation());
        
        if (isConsecutive) {
            stubbed.getFirst().addAnswer(answer);
        } else {
            stubbed.addFirst(new StubbedInvocationMatcher(invocationForStubbing, answer));
        }
    } 
    
    public boolean hasResultFor(Invocation invocation) {
        return findMatch(invocation) != null;
    }
    
    public Object getResultFor(Invocation invocation) throws Throwable {
        return findMatch(invocation).answer(invocation);
    }

    private StubbedInvocationMatcher findMatch(Invocation invocation) {
        for (StubbedInvocationMatcher s : stubbed) {
            if (s.matches(invocation)) {
                return s;
            }
        }
        
        return null;
    }

    //TODO it's not for void method any more
    public void addAnswerForVoidMethod(Answer answer) {
        answersForVoidMethod.add(answer);
    }
    
    //TODO dodgy name
    public void addAnswersForVoidMethod(List<Answer> answers) {
        answersForVoidMethod.addAll(answers);
    }

    public boolean hasAnswerForVoidMethod() {
        return !answersForVoidMethod.isEmpty();
    }

    //TODO it's not for void method any more
    public void addVoidMethodForStubbing(InvocationMatcher voidMethodInvocationMatcher) {
        invocationForStubbing = voidMethodInvocationMatcher;
        assert hasAnswerForVoidMethod();
        for (int i = 0; i < answersForVoidMethod.size(); i++) {
            addAnswer(answersForVoidMethod.get(i), i != 0);
        }
        answersForVoidMethod.clear();
    }
}