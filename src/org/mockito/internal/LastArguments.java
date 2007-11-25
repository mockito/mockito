/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.util.*;

import org.mockito.exceptions.InvalidUseOfMatchersException;
import org.mockito.internal.matchers.*;

public class LastArguments {
    
    static LastArguments INSTANCE = new LastArguments();
    
    private final ThreadLocal<Stack<Object[]>> threadToCurrentArguments = new ThreadLocal<Stack<Object[]>>();
    private final ThreadLocal<Stack<IArgumentMatcher>> threadToArgumentMatcherStack = new ThreadLocal<Stack<IArgumentMatcher>>();

    public static LastArguments instance() {
        return INSTANCE;
    }
    
    public synchronized void reportMatcher(IArgumentMatcher matcher) {
        Stack<IArgumentMatcher> stack = threadToArgumentMatcherStack.get();
        if (stack == null) {
            stack = new Stack<IArgumentMatcher>();
            threadToArgumentMatcherStack.set(stack);
        }
        stack.push(matcher);
    }

    public synchronized List<IArgumentMatcher> pullMatchers() {
        Stack<IArgumentMatcher> stack = threadToArgumentMatcherStack.get();
        if (stack == null) {
            return null;
        }
        
        threadToArgumentMatcherStack.remove();
        return new ArrayList<IArgumentMatcher>(stack);
    }

    public synchronized void reportAnd(int count) {
        Stack<IArgumentMatcher> stack = threadToArgumentMatcherStack.get();
        assertState(stack != null, missingMatchers());
        stack.push(new And(popLastArgumentMatchers(count)));
    }

    private String missingMatchers() {
        return "No matchers found.";
    }

    public synchronized void reportNot() {
        Stack<IArgumentMatcher> stack = threadToArgumentMatcherStack.get();
        assertState(stack != null, missingMatchers());
        stack.push(new Not(popLastArgumentMatchers(1).get(0)));
    }

    private List<IArgumentMatcher> popLastArgumentMatchers(int count) {
        Stack<IArgumentMatcher> stack = threadToArgumentMatcherStack.get();
        assertState(stack != null, missingMatchers());
        assertState(stack.size() >= count,
                "" + count + " matchers expected, " + stack.size() + " recorded.");
        List<IArgumentMatcher> result = new LinkedList<IArgumentMatcher>();
        result.addAll(stack.subList(stack.size() - count, stack.size()));
        for (int i = 0; i < count; i++) {
            stack.pop();
        }
        return result;
    }

    private void assertState(boolean toAssert, String message) {
        if (!toAssert) {
            threadToArgumentMatcherStack.remove();
            throw new InvalidUseOfMatchersException(message);
        }
    }

    public void reportOr(int count) {
        Stack<IArgumentMatcher> stack = threadToArgumentMatcherStack.get();
        assertState(stack != null, missingMatchers());
        stack.push(new Or(popLastArgumentMatchers(count)));
    }

    public void pushCurrentArguments(Object[] args) {
        Stack<Object[]> stack = threadToCurrentArguments.get();
        if (stack == null) {
            stack = new Stack<Object[]>();
            threadToCurrentArguments.set(stack);
        }
        stack.push(args);
    }

    public void popCurrentArguments() {
        Stack<Object[]> stack = threadToCurrentArguments.get();
        stack.pop();
    }
}
