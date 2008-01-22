/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.mockito.exceptions.Printable;
import org.mockito.internal.matchers.ArgumentMatcher;

@SuppressWarnings("unchecked")
public class InvocationMatcher implements Printable {

    private final Invocation invocation;
    private final List<ArgumentMatcher> matchers;

    public InvocationMatcher(Invocation invocation, List<ArgumentMatcher> matchers) {
        if (matchers == null) {
            throw new IllegalArgumentException("matchers cannot be null");
        }
        this.invocation = invocation;
        this.matchers = matchers;
    }
    
    public InvocationMatcher(Invocation invocation) {
        this(invocation, Collections.<ArgumentMatcher>emptyList());
    }
    
    public Method getMethod() {
        return invocation.getMethod();
    }
    
    public Invocation getInvocation() {
        return this.invocation;
    }
    
    public List<ArgumentMatcher> getMatchers() {
        return this.matchers;
    }

    public boolean matches(Invocation actual) {
        return invocation.getMock().equals(actual.getMock())
                && invocation.getMethod().equals(actual.getMethod())
                && argumentsMatch(actual.getArguments());
    }
    
    public boolean differsWithArgumentTypes(Invocation actual) {
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
    
    public String toString() {
        return invocation.toString(matchers);
    }
    
    public String toStringWithArgumentTypes() {
        return invocation.toStringWithArgumentTypes();
    }
}