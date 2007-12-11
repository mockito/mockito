/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import org.mockito.internal.invocation.InvocationMatcher;

public class StubbedInvocationMatcher extends InvocationMatcher {

    private final Result result;
    
    public StubbedInvocationMatcher(InvocationMatcher invocation, Result result) {
        super(invocation.getInvocation(), invocation.getMatchers());
        this.result = result;
    }

    public Result getResult() {
        return result;
    }
    
    @Override
    public String toString() {
        return super.toString() + " stubbed with: " + result.toString();
    }
}