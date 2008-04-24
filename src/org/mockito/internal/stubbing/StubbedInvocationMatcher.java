/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import java.util.LinkedList;

import org.mockito.internal.invocation.InvocationMatcher;

@SuppressWarnings("unchecked")
public class StubbedInvocationMatcher extends InvocationMatcher {

    private final LinkedList<IAnswer> answers = new LinkedList<IAnswer>();
    
    public StubbedInvocationMatcher(InvocationMatcher invocation, IAnswer result) {
        super(invocation.getInvocation(), invocation.getMatchers());
        this.answers.add(result);
    }

    public Object answer() throws Throwable {
        return answers.size() == 1 ? answers.getFirst().answer() : answers.removeFirst().answer();
    }

    public void addResult(IAnswer answer) {
        answers.add(answer);
    }
    
    @Override
    public String toString() {
        return super.toString() + " stubbed with: " + answers;
    }
}