/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.internal.matchers.And;
import org.mockito.internal.matchers.ArgumentMatcher;
import org.mockito.internal.matchers.Not;
import org.mockito.internal.matchers.Or;

//TODO should be similar pattern to MP
@SuppressWarnings("unchecked")
public class LastArguments {
    
    private static final ThreadLocal<LastArguments> INSTANCE = new ThreadLocal<LastArguments>();
    
    private Stack<ArgumentMatcher> matcherStack = new Stack<ArgumentMatcher>();

    public static LastArguments instance() {
        if (INSTANCE.get() == null) {
            INSTANCE.set(new LastArguments()); 
        }
        
        return INSTANCE.get();
    }
    
    public ReturnValues reportMatcher(ArgumentMatcher matcher) {
        matcherStack.push(matcher);
        return new ReturnValues();
    }

    public List<ArgumentMatcher> pullMatchers() {
        if (matcherStack.isEmpty()) {
            return null;
        }
        
        ArrayList<ArgumentMatcher> matchers = new ArrayList<ArgumentMatcher>(matcherStack);
        matcherStack.clear();
        return matchers;
    }

    public void reportAnd(int count) {
        assertState(!matcherStack.isEmpty(), "No matchers found for And(?).");
        matcherStack.push(new And(popLastArgumentMatchers(count)));
    }

    public void reportNot() {
        assertState(!matcherStack.isEmpty(), "No matchers found for Not(?).");
        matcherStack.push(new Not(popLastArgumentMatchers(1).get(0)));
    }

    private List<ArgumentMatcher> popLastArgumentMatchers(int count) {
        assertState(!matcherStack.isEmpty(), "No matchers found.");
        assertState(matcherStack.size() >= count,
                "" + count + " matchers expected, " + matcherStack.size() + " recorded.");
        List<ArgumentMatcher> result = new LinkedList<ArgumentMatcher>();
        result.addAll(matcherStack.subList(matcherStack.size() - count, matcherStack.size()));
        for (int i = 0; i < count; i++) {
            matcherStack.pop();
        }
        return result;
    }

    private void assertState(boolean toAssert, String message) {
        if (!toAssert) {
            matcherStack.clear();
            throw new InvalidUseOfMatchersException(message);
        }
    }

    public void reportOr(int count) {
        assertState(!matcherStack.isEmpty(), "No matchers found.");
        matcherStack.push(new Or(popLastArgumentMatchers(count)));
    }
}
