/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

import java.util.ArrayList;
import java.util.List;

public class UnorderedBehavior {

    private final List<ExpectedInvocationAndResults> results = new ArrayList<ExpectedInvocationAndResults>();

    private final boolean checkOrder;

    public UnorderedBehavior(boolean checkOrder) {
        this.checkOrder = checkOrder;
    }

    public void addExpected(ExpectedInvocation expected, Result result,
            Range count) {
        for (ExpectedInvocationAndResults entry : results) {
            if (entry.getExpectedInvocation().equals(expected)) {
                entry.getResults().add(result, count);
                return;
            }
        }
        Results list = new Results();
        list.add(result, count);
        results.add(new ExpectedInvocationAndResults(expected, list));
    }

    public Result addActual(Invocation actual) {
        for (ExpectedInvocationAndResults entry : results) {
            if (!entry.getExpectedInvocation().matches(actual)) {
                continue;
            }
            Result result = entry.getResults().next();
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public boolean verify() {
        for (ExpectedInvocationAndResults entry : results) {
            if (!entry.getResults().hasValidCallCount()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return toString(null);
    }

    public String toString(Invocation invocation) {
        StringBuffer result = new StringBuffer();
        for (ExpectedInvocationAndResults entry : results) {
            boolean unordered = !checkOrder;
            boolean validCallCount = entry.getResults().hasValidCallCount();
            boolean match = invocation != null
                    && entry.getExpectedInvocation().matches(invocation);

            if (unordered && validCallCount && !match) {
                continue;
            }
            result.append("\n    " + entry.toString());
            if (match) {
                result.append(" (+1)");
            }
        }
        return result.toString();
    }

    public boolean allowsExpectedInvocation(ExpectedInvocation expected,
            boolean checkOrder) {
        if (this.checkOrder != checkOrder) {
            return false;
        } else if (results.isEmpty() || !this.checkOrder) {
            return true;
        } else {
            ExpectedInvocation lastMethodCall = results.get(results.size() - 1)
                    .getExpectedInvocation();
            return lastMethodCall.equals(expected);
        }
    }

}