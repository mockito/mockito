/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.invocation.StubInfoImpl;
import org.mockito.internal.verification.DefaultRegisteredInvocations;
import org.mockito.internal.verification.RegisteredInvocations;
import org.mockito.internal.verification.SingleRegisteredInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationContainer;
import org.mockito.invocation.MatchableInvocation;
import org.mockito.mock.MockCreationSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubbing;
import org.mockito.stubbing.ValidableAnswer;

@SuppressWarnings("unchecked")
public class InvocationContainerImpl implements InvocationContainer, Serializable {

    private static final long serialVersionUID = -5334301962749537177L;
    private final LinkedList<StubbedInvocationMatcher> stubbed = new LinkedList<>();
    private final DoAnswerStyleStubbing doAnswerStyleStubbing;
    private final RegisteredInvocations registeredInvocations;
    private final Strictness mockStrictness;

    private MatchableInvocation invocationForStubbing;

    public InvocationContainerImpl(MockCreationSettings mockSettings) {
        this.registeredInvocations = createRegisteredInvocations(mockSettings);
        this.mockStrictness = mockSettings.isLenient() ? Strictness.LENIENT : null;
        this.doAnswerStyleStubbing = new DoAnswerStyleStubbing();
    }

    public synchronized void setInvocationForPotentialStubbing(MatchableInvocation invocation) {
        registeredInvocations.add(invocation.getInvocation());
        this.invocationForStubbing = invocation;
    }

    public synchronized void resetInvocationForPotentialStubbing(
            MatchableInvocation invocationMatcher) {
        this.invocationForStubbing = invocationMatcher;
    }

    public synchronized void addAnswer(Answer answer, Strictness stubbingStrictness) {
        registeredInvocations.removeLast();
        addAnswer(answer, false, stubbingStrictness);
    }

    /** Adds new stubbed answer and returns the invocation matcher the answer was added to. */
    public synchronized StubbedInvocationMatcher addAnswer(
            Answer answer, boolean isConsecutive, Strictness stubbingStrictness) {
        Invocation invocation = invocationForStubbing.getInvocation();
        mockingProgress().stubbingCompleted();
        if (answer instanceof ValidableAnswer) {
            ((ValidableAnswer) answer).validateFor(invocation);
        }

        if (isConsecutive) {
            stubbed.getFirst().addAnswer(answer);
        } else {
            Strictness effectiveStrictness =
                    stubbingStrictness != null ? stubbingStrictness : this.mockStrictness;
            stubbed.addFirst(
                    new StubbedInvocationMatcher(
                            answer, invocationForStubbing, effectiveStrictness));
        }
        return stubbed.getFirst();
    }

    public synchronized void addConsecutiveAnswer(Answer answer) {
        addAnswer(answer, true, null);
    }

    Object answerTo(Invocation invocation) throws Throwable {
        return findAnswerFor(invocation).answer(invocation);
    }

    public synchronized StubbedInvocationMatcher findAnswerFor(Invocation invocation) {
        for (StubbedInvocationMatcher s : stubbed) {
            if (s.matches(invocation)) {
                s.markStubUsed(invocation);
                // TODO we should mark stubbed at the point of stubbing, not at the point where
                // the stub is being used
                invocation.markStubbed(new StubInfoImpl(s));
                return s;
            }
        }

        return null;
    }

    /**
     * Sets the answers declared with 'doAnswer' style.
     */
    public synchronized void setAnswersForStubbing(List<Answer<?>> answers, Strictness strictness) {
        doAnswerStyleStubbing.setAnswers(answers, strictness);
    }

    public synchronized boolean hasAnswersForStubbing() {
        return !doAnswerStyleStubbing.isSet();
    }

    public synchronized boolean hasInvocationForPotentialStubbing() {
        return !registeredInvocations.isEmpty();
    }

    public synchronized void setMethodForStubbing(MatchableInvocation invocation) {
        invocationForStubbing = invocation;
        assert hasAnswersForStubbing();
        for (int i = 0; i < doAnswerStyleStubbing.getAnswers().size(); i++) {
            addAnswer(
                    doAnswerStyleStubbing.getAnswers().get(i),
                    i != 0,
                    doAnswerStyleStubbing.getStubbingStrictness());
        }
        doAnswerStyleStubbing.clear();
    }

    @Override
    public String toString() {
        return "invocationForStubbing: " + invocationForStubbing;
    }

    public synchronized List<Invocation> getInvocations() {
        return registeredInvocations.getAll();
    }

    public synchronized void clearInvocations() {
        registeredInvocations.clear();
    }

    /**
     * Stubbings in ascending order, most recent last
     */
    public synchronized Collection<Stubbing> getStubbingsAscending() {
        List<Stubbing> result = new LinkedList<>(stubbed);
        Collections.reverse(result);
        return result;
    }

    public synchronized Object invokedMock() {
        return invocationForStubbing.getInvocation().getMock();
    }

    private RegisteredInvocations createRegisteredInvocations(MockCreationSettings mockSettings) {
        return mockSettings.isStubOnly()
                ? new SingleRegisteredInvocation()
                : new DefaultRegisteredInvocations();
    }

    public synchronized Answer findStubbedAnswer() {
        for (StubbedInvocationMatcher s : stubbed) {
            if (invocationForStubbing.matches(s.getInvocation())) {
                return s;
            }
        }
        return null;
    }
}
