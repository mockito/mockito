/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.hamcrest.Matcher;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.internal.matchers.And;
import org.mockito.internal.matchers.Not;
import org.mockito.internal.matchers.Or;

@SuppressWarnings("unchecked")
public class LastArguments {
    
    private static final ThreadLocal<LastArguments> INSTANCE = new ThreadLocal<LastArguments>();
    
    private Stack<Matcher> matcherStack = new Stack<Matcher>();

    public static LastArguments instance() {
        if (INSTANCE.get() == null) {
            INSTANCE.set(new LastArguments()); 
        }
        
        return INSTANCE.get();
    }
    
    public EmptyReturnValues reportMatcher(Matcher matcher) {
        matcherStack.push(matcher);
        return new EmptyReturnValues();
    }

    public List<Matcher> pullMatchers() {
        if (matcherStack.isEmpty()) {
            return null;
        }
        
        ArrayList<Matcher> matchers = new ArrayList<Matcher>(matcherStack);
        matcherStack.clear();
        return matchers;
    }

    public EmptyReturnValues reportAnd() {
        assertState(!matcherStack.isEmpty(), "No matchers found for And(?).");
        matcherStack.push(new And(popLastArgumentMatchers(2)));
        return new EmptyReturnValues();
    }

    public EmptyReturnValues reportNot() {
        assertState(!matcherStack.isEmpty(), "No matchers found for Not(?).");
        matcherStack.push(new Not(popLastArgumentMatchers(1).get(0)));
        return new EmptyReturnValues();
    }

    private List<Matcher> popLastArgumentMatchers(int count) {
        assertState(!matcherStack.isEmpty(), "No matchers found.");
        assertState(matcherStack.size() >= count,
                "" + count + " matchers expected, " + matcherStack.size() + " recorded.");
        List<Matcher> result = new LinkedList<Matcher>();
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

    public EmptyReturnValues reportOr() {
        assertState(!matcherStack.isEmpty(), "No matchers found.");
        matcherStack.push(new Or(popLastArgumentMatchers(2)));
        return new EmptyReturnValues();
    }
}
