/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.easymock.IArgumentMatcher;
import org.easymock.internal.matchers.Equals;

public class ExpectedInvocation {

    protected final Invocation invocation;

    @SuppressWarnings("deprecation")
    private final org.easymock.ArgumentsMatcher matcher;

    private final List<IArgumentMatcher> matchers;

    public ExpectedInvocation(Invocation invocation,
            List<IArgumentMatcher> matchers) {
        this(invocation, matchers, null);
    }

    private ExpectedInvocation(Invocation invocation,
            List<IArgumentMatcher> matchers, @SuppressWarnings("deprecation")
            org.easymock.ArgumentsMatcher matcher) {
        this.invocation = invocation;
        this.matcher = matcher;
        this.matchers = (matcher == null) ? createMissingMatchers(invocation,
                matchers) : null;
    }

    private List<IArgumentMatcher> createMissingMatchers(Invocation invocation,
            List<IArgumentMatcher> matchers) {
        if (matchers != null) {
            if (matchers.size() != invocation.getArguments().length) {
                throw new IllegalStateException(""
                        + invocation.getArguments().length
                        + " matchers expected, " + matchers.size()
                        + " recorded.");
            }
            ;
            return matchers;
        }
        List<IArgumentMatcher> result = new ArrayList<IArgumentMatcher>();
        for (Object argument : invocation.getArguments()) {
            result.add(new Equals(argument));
        }
        return result;
    }

    public boolean equals(Object o) {
        if (o == null || !this.getClass().equals(o.getClass()))
            return false;

        ExpectedInvocation other = (ExpectedInvocation) o;
        return this.invocation.equals(other.invocation)
                && ((this.matcher == null && other.matcher == null) || (this.matcher != null && this.matcher
                        .equals(other.matcher)))
                && ((this.matchers == null && other.matchers == null) || (this.matchers != null && this.matchers
                        .equals(other.matchers)));
    }

    public int hashCode() {
        return 1;
    }

    public boolean matches(Invocation actual) {
        return matchers != null ? this.invocation.getMock().equals(
                actual.getMock())
                && this.invocation.getMethod().equals(actual.getMethod())
                && matches(actual.getArguments()) : this.invocation.matches(
                actual, matcher);
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
        return matchers != null ? myToString() : invocation.toString(matcher);
    }

    private String myToString() {
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

    public ExpectedInvocation withMatcher(@SuppressWarnings("deprecation")
    org.easymock.ArgumentsMatcher matcher) {
        return new ExpectedInvocation(invocation, null, matcher);
    }
}
