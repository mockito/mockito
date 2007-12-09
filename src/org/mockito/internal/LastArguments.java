/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.util.*;

import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.internal.matchers.*;

public class LastArguments {
    
    private static ThreadLocal<LastArguments> INSTANCE = new ThreadLocal<LastArguments>();
    
    private Stack<IArgumentMatcher> matcherStack = new Stack<IArgumentMatcher>();

    public static LastArguments instance() {
        if (INSTANCE.get() == null) {
            INSTANCE.set(new LastArguments()); 
        }
        
        return INSTANCE.get();
    }
    
    public void reportMatcher(IArgumentMatcher matcher) {
        matcherStack.push(matcher);
    }

    public List<IArgumentMatcher> pullMatchers() {
        if (matcherStack.isEmpty()) {
            return null;
        }
        
        ArrayList<IArgumentMatcher> matchers = new ArrayList<IArgumentMatcher>(matcherStack);
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

    private List<IArgumentMatcher> popLastArgumentMatchers(int count) {
        assertState(!matcherStack.isEmpty(), "No matchers found.");
        assertState(matcherStack.size() >= count,
                "" + count + " matchers expected, " + matcherStack.size() + " recorded.");
        List<IArgumentMatcher> result = new LinkedList<IArgumentMatcher>();
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
