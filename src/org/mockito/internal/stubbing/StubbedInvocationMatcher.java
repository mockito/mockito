/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import java.util.LinkedList;

import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

@SuppressWarnings("unchecked")
public class StubbedInvocationMatcher extends InvocationMatcher {

    private final LinkedList<Answer> answers = new LinkedList<Answer>();

    public StubbedInvocationMatcher(InvocationMatcher invocation, Answer answer) {
        super(invocation.getInvocation(), invocation.getMatchers());
        this.answers.add(answer);
    }

    public Object answer(Invocation invocation) throws Throwable {
        return answers.size() == 1 ? answers.getFirst().answer(invocation) : answers.removeFirst().answer(invocation);
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    @Override
    public String toString() {
        return super.toString() + " stubbed with: " + answers;
    }
}