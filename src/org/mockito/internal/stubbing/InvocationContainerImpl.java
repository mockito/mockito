/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.StubInfo;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.internal.verification.RegisteredInvocations;
import org.mockito.stubbing.Answer;

@SuppressWarnings("unchecked")
public class InvocationContainerImpl implements InvocationContainer, Serializable {

    private static final long serialVersionUID = -5334301962749537176L;
    private final LinkedList<StubbedInvocationMatcher> stubbed = new LinkedList<StubbedInvocationMatcher>();
    private final MockingProgress mockingProgress;
    private final List<Answer> answersForStubbing = new ArrayList<Answer>();
    private final RegisteredInvocations registeredInvocations = new RegisteredInvocations();

    private InvocationMatcher invocationForStubbing;

    public InvocationContainerImpl(MockingProgress mockingProgress) {
        this.mockingProgress = mockingProgress;
    }

    public void setInvocationForPotentialStubbing(InvocationMatcher invocation) {
        registeredInvocations.add(invocation.getInvocation());
        this.invocationForStubbing = invocation;
    }

    public void resetInvocationForPotentialStubbing(InvocationMatcher invocationMatcher) {
        this.invocationForStubbing = invocationMatcher;
    }

    public void addAnswer(Answer answer) {
        registeredInvocations.removeLast();
        addAnswer(answer, false);
    }

    public void addConsecutiveAnswer(Answer answer) {
        addAnswer(answer, true);
    }

    public void addAnswer(Answer answer, boolean isConsecutive) {
        Invocation invocation = invocationForStubbing.getInvocation();
        mockingProgress.stubbingCompleted(invocation);
        AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.validate(answer, invocation);

        synchronized (stubbed) {
            if (isConsecutive) {
                stubbed.getFirst().addAnswer(answer);
            } else {
                stubbed.addFirst(new StubbedInvocationMatcher(invocationForStubbing, answer));
            }
        }
    }

    Object answerTo(Invocation invocation) throws Throwable {
        return findAnswerFor(invocation).answer(invocation);
    }

    public StubbedInvocationMatcher findAnswerFor(Invocation invocation) {
        synchronized (stubbed) {
            for (StubbedInvocationMatcher s : stubbed) {
                if (s.matches(invocation)) {
                    s.markStubUsed(invocation);
                    invocation.markStubbed(new StubInfo(s));
                    return s;
                }
            }
        }

        return null;
    }

    public void addAnswerForVoidMethod(Answer answer) {
        answersForStubbing.add(answer);
    }

    public void setAnswersForStubbing(List<Answer> answers) {
        answersForStubbing.addAll(answers);
    }

    public boolean hasAnswersForStubbing() {
        return !answersForStubbing.isEmpty();
    }

    public void setMethodForStubbing(InvocationMatcher invocation) {
        invocationForStubbing = invocation;
        assert hasAnswersForStubbing();
        for (int i = 0; i < answersForStubbing.size(); i++) {
            addAnswer(answersForStubbing.get(i), i != 0);
        }
        answersForStubbing.clear();
    }

    @Override
    public String toString() {
        return "invocationForStubbing: " + invocationForStubbing;
    }

    public List<Invocation> getInvocations() {
        return registeredInvocations.getAll();
    }

    public List<StubbedInvocationMatcher> getStubbedInvocations() {
        return stubbed;
    }
}
