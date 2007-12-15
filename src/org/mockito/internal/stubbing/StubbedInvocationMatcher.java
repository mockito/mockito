/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.internal.invocation.InvocationMatcher;

@SuppressWarnings("unchecked")
public class StubbedInvocationMatcher extends InvocationMatcher {

    private final IAnswer result;
    
    public StubbedInvocationMatcher(InvocationMatcher invocation, IAnswer result) {
        super(invocation.getInvocation(), invocation.getMatchers());
        this.result = result;
    }

    public Object answer() throws Throwable {
        return result.answer();
    }
    
    @Override
    public String toString() {
        return super.toString() + " stubbed with: " + result.toString();
    }
}