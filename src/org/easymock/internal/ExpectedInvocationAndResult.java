/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

public class ExpectedInvocationAndResult {
    ExpectedInvocation expectedInvocation;

    Result result;

    public ExpectedInvocationAndResult(ExpectedInvocation expectedInvocation,
            Result result) {
        this.expectedInvocation = expectedInvocation;
        this.result = result;
    }

    public ExpectedInvocation getExpectedInvocation() {
        return expectedInvocation;
    }

    public Result getResult() {
        return result;
    }
}