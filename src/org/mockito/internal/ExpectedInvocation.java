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
        this.invocation = invocation;
        this.matchers = matchers;
    }
    
    //TODO add test that makes sure sequenceNumber doesnt take part in equals()
    public boolean equals(Object o) {
        if (o == null || !this.getClass().equals(o.getClass()))
            return false;

        ExpectedInvocation other = (ExpectedInvocation) o;
        return this.invocation.equals(other.invocation)
                && ((this.matchers == null && other.matchers == null) || (this.matchers != null && this.matchers
                        .equals(other.matchers)));
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
        StringBuffer result = new StringBuffer();
        result.append(invocation.getMockAndMethodName());
        result.append("(");
        for (Iterator<IArgumentMatcher> it = matchers.iterator(); it.hasNext();) {
            it.next().appendTo(result);
            if (it.hasNext()) {
                result.append(", ");
            }
        }
        result.append(")");
        return result.toString();
    }

    public Method getMethod() {
        return invocation.getMethod();
    }
    
    public Invocation getInvocation() {
        return this.invocation;
    }
}