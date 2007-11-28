/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import java.lang.reflect.Method;
import java.util.*;

import org.mockito.internal.matchers.IArgumentMatcher;

public class ExpectedInvocation {

    protected final Invocation invocation;
    private final List<IArgumentMatcher> matchers;

    public ExpectedInvocation(Invocation invocation, List<IArgumentMatcher> matchers) {
        if (matchers == null) {
            throw new IllegalArgumentException("matchers cannot be null");
        }
        this.invocation = invocation;
        this.matchers = matchers;
    }
    
    public ExpectedInvocation(Invocation invocation) {
        this(invocation, Collections.<IArgumentMatcher>emptyList());
    }
    
    public boolean equals(Object o) {
        if (o == null || !this.getClass().equals(o.getClass()))
            return false;

        ExpectedInvocation other = (ExpectedInvocation) o;
        return invocation.equals(other.invocation) && matchers.equals(other.matchers);
    }

    public int hashCode() {
        return 1;
    }

    public boolean matches(Invocation actual) {
        return this.invocation.getMock().equals(
                actual.getMock())
                && this.invocation.getMethod().equals(actual.getMethod())
                && matches(actual.getArguments());
    }

    private boolean matches(Object[] arguments) {
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

    public String toString() {
        return invocation.toString(matchers);
    }

    public Method getMethod() {
        return invocation.getMethod();
    }
    
    public Invocation getInvocation() {
        return this.invocation;
    }
}