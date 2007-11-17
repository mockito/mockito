/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.easymock.ArgumentsMatcher;
import org.easymock.MockControl;
import org.mockito.internal.*;

public class MocksBehavior implements IMocksBehavior {

    private final List<UnorderedBehavior> behaviorLists = new ArrayList<UnorderedBehavior>();

    private List<ExpectedInvocationAndResult> stubResults = new ArrayList<ExpectedInvocationAndResult>();

    private final boolean nice;

    private boolean checkOrder;

    private int position = 0;

    public MocksBehavior(boolean nice) {
        this.nice = nice;
    }

    public final void addStub(ExpectedInvocation expected, Result result) {
        stubResults.add(new ExpectedInvocationAndResult(expected, result));
    }

    public void addExpected(ExpectedInvocation expected, Result result,
            Range count) {
        addBehaviorListIfNecessary(expected);
        lastBehaviorList().addExpected(expected, result, count);
    }

    private final Result getStubResult(Invocation actual) {
        for (ExpectedInvocationAndResult each : stubResults) {
            if (each.getExpectedInvocation().matches(actual)) {
                return each.getResult();
            }
        }
        return null;
    }

    private void addBehaviorListIfNecessary(ExpectedInvocation expected) {
        if (behaviorLists.isEmpty()
                || !lastBehaviorList().allowsExpectedInvocation(expected,
                        checkOrder)) {
            behaviorLists.add(new UnorderedBehavior(checkOrder));
        }
    }

    private UnorderedBehavior lastBehaviorList() {
        return behaviorLists.get(behaviorLists.size() - 1);
    }

    public final Result addActual(Invocation actual) {
        int tempPosition = position;
        String errorMessage = "";
        while (position < behaviorLists.size()) {
            Result result = behaviorLists.get(position).addActual(actual);
            if (result != null) {
                return result;
            }
            errorMessage += behaviorLists.get(position).toString(actual);
            if (!behaviorLists.get(position).verify()) {
                break;
            }
            position++;
        }
        Result stubOrNice = getStubResult(actual);
        if (stubOrNice == null && nice) {
            stubOrNice = Result.createReturnResult(ToTypeMappings.emptyReturnValueFor(actual.getMethod().getReturnType()));
        }
        if (stubOrNice != null) {
            position = tempPosition;
            return stubOrNice;
        }
        throw new AssertionErrorWrapper(new AssertionError(
                "\n  Unexpected method call "
                        + actual.toString() + ":"
                        + errorMessage.toString()));
    }

    public void verify() {
        boolean verified = true;
        StringBuffer errorMessage = new StringBuffer();

        for (UnorderedBehavior behaviorList : behaviorLists.subList(position,
                behaviorLists.size())) {
            errorMessage.append(behaviorList.toString());
            if (!behaviorList.verify()) {
                verified = false;
            }
        }
        if (verified) {
            return;
        }

        throw new AssertionErrorWrapper(new AssertionError(
                "\n  Expectation failure on verify:" + errorMessage.toString()));
    }

    public void checkOrder(boolean value) {
        this.checkOrder = value;
    }

    private LegacyMatcherProvider legacyMatcherProvider;

    public LegacyMatcherProvider getLegacyMatcherProvider() {
        if (legacyMatcherProvider == null) {
            legacyMatcherProvider = new LegacyMatcherProvider();
        }
        return legacyMatcherProvider;
    }

    public void setDefaultMatcher(ArgumentsMatcher matcher) {
        getLegacyMatcherProvider().setDefaultMatcher(matcher);
    }

    public void setMatcher(Method method, ArgumentsMatcher matcher) {
        getLegacyMatcherProvider().setMatcher(method, matcher);
    }
}
