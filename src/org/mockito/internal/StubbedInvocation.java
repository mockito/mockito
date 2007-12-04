/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;


public class StubbedInvocation extends ExpectedInvocation {

    private final Result result;

    public StubbedInvocation(ExpectedInvocation invocation, Result result) {
        super(invocation.invocation, invocation.matchers);
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