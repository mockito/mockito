/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

import java.util.*;

import org.easymock.IArgumentMatcher;
import org.easymock.internal.matchers.And;
import org.easymock.internal.matchers.Not;
import org.easymock.internal.matchers.Or;

public class LastArguments {
    private static final ThreadLocal<Stack<Object[]>> threadToCurrentArguments = new ThreadLocal<Stack<Object[]>>();

    private static final ThreadLocal<Stack<IArgumentMatcher>> threadToArgumentMatcherStack = new ThreadLocal<Stack<IArgumentMatcher>>();

    public static synchronized void reportMatcher(IArgumentMatcher matcher) {
        Stack<IArgumentMatcher> stack = threadToArgumentMatcherStack.get();
        if (stack == null) {
            stack = new Stack<IArgumentMatcher>();
            threadToArgumentMatcherStack.set(stack);
        }
        stack.push(matcher);
    }

    public static synchronized List<IArgumentMatcher> pullMatchers() {
        Stack<IArgumentMatcher> stack = threadToArgumentMatcherStack.get();
        if (stack == null) {
            return null;
        }
        threadToArgumentMatcherStack.remove();
        return new ArrayList<IArgumentMatcher>(stack);
    }

    public static synchronized void reportAnd(int count) {
        Stack<IArgumentMatcher> stack = threadToArgumentMatcherStack.get();
        assertState(stack != null, "no matchers found.");
        stack.push(new And(popLastArgumentMatchers(count)));
    }

    public static synchronized void reportNot() {
        Stack<IArgumentMatcher> stack = threadToArgumentMatcherStack.get();
        assertState(stack != null, "no matchers found.");
        stack.push(new Not(popLastArgumentMatchers(1).get(0)));
    }

    private static List<IArgumentMatcher> popLastArgumentMatchers(int count) {
        Stack<IArgumentMatcher> stack = threadToArgumentMatcherStack.get();
        assertState(stack != null, "no matchers found.");
        assertState(stack.size() >= count, "" + count + " matchers expected, "
                + stack.size() + " recorded.");
        List<IArgumentMatcher> result = new LinkedList<IArgumentMatcher>();
        result.addAll(stack.subList(stack.size() - count, stack.size()));
        for (int i = 0; i < count; i++) {
            stack.pop();
        }
        return result;
    }

    private static void assertState(boolean toAssert, String message) {
        if (!toAssert) {
            threadToArgumentMatcherStack.remove();
            throw new IllegalStateException(message);
        }
    }

    public static void reportOr(int count) {
        Stack<IArgumentMatcher> stack = threadToArgumentMatcherStack.get();
        assertState(stack != null, "no matchers found.");
        stack.push(new Or(popLastArgumentMatchers(count)));
    }

    public static Object[] getCurrentArguments() {
        Stack<Object[]> stack = threadToCurrentArguments.get();
        if (stack == null || stack.empty()) {
            return null;
        }
        return stack.lastElement();
    }

    public static void pushCurrentArguments(Object[] args) {
        Stack<Object[]> stack = threadToCurrentArguments.get();
        if (stack == null) {
            stack = new Stack<Object[]>();
            threadToCurrentArguments.set(stack);
        }
        stack.push(args);
    }

    public static void popCurrentArguments() {
        Stack<Object[]> stack = threadToCurrentArguments.get();
        stack.pop();
    }
}
