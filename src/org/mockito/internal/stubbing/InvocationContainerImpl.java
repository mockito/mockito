/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.StubInfoImpl;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.stubbing.answers.AnswersValidator;
import org.mockito.internal.verification.DefaultRegisteredInvocations;
import org.mockito.internal.verification.RegisteredInvocations;
import org.mockito.internal.verification.SingleRegisteredInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Answer;

@SuppressWarnings("rawtypes")
public class InvocationContainerImpl implements InvocationContainer, Serializable {

    private static final long serialVersionUID = -5334301962749537177L;
    private final LinkedList<StubbedInvocationMatcher> stubbed = new LinkedList<StubbedInvocationMatcher>();
    private final MockingProgress mockingProgress;
    private final List<Answer> answersForStubbing = new ArrayList<Answer>();
    private final RegisteredInvocations registeredInvocations;

    private InvocationMatcher invocationForStubbing;

    public InvocationContainerImpl(final MockingProgress mockingProgress, final MockCreationSettings<?> mockSettings) {
        this.mockingProgress = mockingProgress;
        this.registeredInvocations = createRegisteredInvocations(mockSettings);
    }

    public void setInvocationForPotentialStubbing(final InvocationMatcher invocation) {
        registeredInvocations.add(invocation.getInvocation());
        this.invocationForStubbing = invocation;
    }

    public void resetInvocationForPotentialStubbing(final InvocationMatcher invocationMatcher) {
        this.invocationForStubbing = invocationMatcher;
    }

    public void addAnswer(final Answer<?> answer) {
        registeredInvocations.removeLast();
        addAnswer(answer, false);
    }

    public void addConsecutiveAnswer(final Answer<?> answer) {
        addAnswer(answer, true);
    }

    public void addAnswer(final Answer<?> answer, final boolean isConsecutive) {
        final Invocation invocation = invocationForStubbing.getInvocation();
        mockingProgress.stubbingCompleted(invocation);
        final AnswersValidator answersValidator = new AnswersValidator();
        answersValidator.validate(answer, invocation);

        synchronized (stubbed) {
            if (isConsecutive) {
                stubbed.getFirst().addAnswer(answer);
            } else {
                stubbed.addFirst(new StubbedInvocationMatcher(invocationForStubbing, answer));
            }
        }
    }

    Object answerTo(final Invocation invocation) throws Throwable {
        return findAnswerFor(invocation).answer(invocation);
    }

    public StubbedInvocationMatcher findAnswerFor(final Invocation invocation) {
        synchronized (stubbed) {
            for (final StubbedInvocationMatcher s : stubbed) {
                if (s.matches(invocation)) {
                    s.markStubUsed(invocation);
                    invocation.markStubbed(new StubInfoImpl(s));
                    return s;
                }
            }
        }

        return null;
    }

    public void addAnswerForVoidMethod(final Answer<?> answer) {
        answersForStubbing.add(answer);
    }

    public void setAnswersForStubbing(final List<Answer> answers) {
        answersForStubbing.addAll(answers);
    }

    public boolean hasAnswersForStubbing() {
        return !answersForStubbing.isEmpty();
    }

    public boolean hasInvocationForPotentialStubbing() {
        return !registeredInvocations.isEmpty();
    }

    public void setMethodForStubbing(final InvocationMatcher invocation) {
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

    public Object invokedMock() {
        return invocationForStubbing.getInvocation().getMock();
    }
    
    public InvocationMatcher getInvocationForStubbing() {
        return invocationForStubbing;
    }

    private RegisteredInvocations createRegisteredInvocations(final MockCreationSettings<?> mockSettings) {
        return mockSettings.isStubOnly()
          ? new SingleRegisteredInvocation()
          : new DefaultRegisteredInvocations();
    }
}
