/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.lang.reflect.Method;
import java.util.List;

import org.hamcrest.Matcher;
import org.mockito.exceptions.PrintableInvocation;

@SuppressWarnings("unchecked")
public class InvocationMatcher implements PrintableInvocation {

    private final Invocation invocation;
    private final List<Matcher> matchers;

    public InvocationMatcher(Invocation invocation, List<Matcher> matchers) {
        this.invocation = invocation;
        if (matchers == null) {
            this.matchers = invocation.argumentsToMatchers();
        } else {
            this.matchers = matchers;
        }
    }
    
    public InvocationMatcher(Invocation invocation) {
        this(invocation, null);
    }

    public Method getMethod() {
        return invocation.getMethod();
    }
    
    public Invocation getInvocation() {
        return this.invocation;
    }
    
    public List<Matcher> getMatchers() {
        return this.matchers;
    }
    
    public String toString() {
        return invocation.toString(matchers);
    }
    
    public String getMethodName() {
        return invocation.getMethodName();
    }
    
    public String getArgs() {
        return invocation.getArgs(matchers);
    }

    public boolean matches(Invocation actual) {
        return invocation.getMock().equals(actual.getMock())
                && invocation.getMethod().equals(actual.getMethod())
                && argumentsMatch(actual.getArguments());
    }
    
    public boolean isOverloadedVersionOf(Invocation actual) {
        return invocation.getMock().equals(actual.getMock())
            && argumentsMatch(actual.getArguments())
            && invocation.getMethod().getName().equals(actual.getMethod().getName())
            && !invocation.getMethod().equals(actual.getMethod());
    }

    private boolean argumentsMatch(Object[] arguments) {
        if (arguments.length != matchers.size()) {
            return false;
        }
        for (int i = 0; i < arguments.length; i++) {
            if (!matchers.get(i).matches(arguments[i])) {
                return false;
            }
        }
        return true;
    }
}