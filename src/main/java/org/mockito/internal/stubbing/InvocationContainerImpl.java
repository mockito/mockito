/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.StubInfoImpl;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.internal.verification.DefaultRegisteredInvocations;
import org.mockito.internal.verification.RegisteredInvocations;
import org.mockito.internal.verification.SingleRegisteredInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Answer;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unchecked")
public class InvocationContainerImpl implements InvocationContainer, Serializable {

    private static final long serialVersionUID = -5334301962749537177L;
    private final LinkedList<StubbedInvocationMatcher> stubbed = new LinkedList<StubbedInvocationMatcher>();
    private final List<Answer<?>> answersForStubbing = new ArrayList<Answer<?>>();
    private final RegisteredInvocations registeredInvocations;

    private InvocationMatcher invocationForStubbing;

    public InvocationContainerImpl(MockCreationSettings mockSettings) {
        this.registeredInvocations = createRegisteredInvocations(mockSettings);
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
        mockingProgress().stubbingCompleted(invocation);
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
                    mockingProgress().getStubbingListener().usedStubbing(s.getInvocation(), invocation);
                    s.markStubUsed(invocation);
                    invocation.markStubbed(new StubInfoImpl(s));
                    return s;
                }
            }
        }

        return null;
    }

    public void addAnswerForVoidMethod(Answer answer) {
        answersForStubbing.add(answer);
    }

    public void setAnswersForStubbing(List<Answer<?>> answers) {
        answersForStubbing.addAll(answers);
    }

    public boolean hasAnswersForStubbing() {
        return !answersForStubbing.isEmpty();
    }

    public boolean hasInvocationForPotentialStubbing() {
        return !registeredInvocations.isEmpty();
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

    public void clearInvocations() {
        registeredInvocations.clear();
    }

    public List<StubbedInvocationMatcher> getStubbedInvocations() {
        return stubbed;
    }

    public Object invokedMock() {
        return invocationForStubbing.getInvocation().getMock();
    }
    
    public InvocationMatcher getInvocationForStubbing() {
        return invocationForStubbing;
    }

    private RegisteredInvocations createRegisteredInvocations(MockCreationSettings mockSettings) {
        return mockSettings.isStubOnly()
          ? new SingleRegisteredInvocation()
          : new DefaultRegisteredInvocations();
    }
}
