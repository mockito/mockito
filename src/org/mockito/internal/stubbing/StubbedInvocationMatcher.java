/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import java.io.Serializable;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class StubbedInvocationMatcher extends InvocationMatcher implements Answer, Serializable {

    private static final long serialVersionUID = 4919105134123672727L;
    private final Queue<Answer> answers = new ConcurrentLinkedQueue<Answer>();
    private DescribedInvocation usedAt;

    public StubbedInvocationMatcher(final InvocationMatcher invocation, final Answer answer) {
        super(invocation.getInvocation(), invocation.getMatchers());
        this.answers.add(answer);
    }

    public Object answer(final InvocationOnMock invocation) throws Throwable {
        //see ThreadsShareGenerouslyStubbedMockTest
        Answer a;
        synchronized(answers) {
            a = answers.size() == 1 ? answers.peek() : answers.poll();
        }
        return a.answer(invocation);
    }

    public void addAnswer(final Answer answer) {
        answers.add(answer);
    }

    public void markStubUsed(final DescribedInvocation usedAt) {
        this.usedAt = usedAt;
    }

    public boolean wasUsed() {
        return usedAt != null;
    }

    @Override
    public String toString() {
        return super.toString() + " stubbed with: " + answers;
    }
}