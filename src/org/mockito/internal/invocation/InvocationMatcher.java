/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.invocation;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.hamcrest.Matcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

@SuppressWarnings("rawtypes")
public class InvocationMatcher implements DescribedInvocation, CapturesArgumentsFromInvocation, Serializable {

    private static final long serialVersionUID = -3047126096857467610L;
    private final Invocation invocation;
    private final List<Matcher> matchers;

    public InvocationMatcher(final Invocation invocation, final List<Matcher> matchers) {
        this.invocation = invocation;
        if (matchers.isEmpty()) {
            this.matchers = ArgumentsProcessor.argumentsToMatchers(invocation.getArguments());
        } else {
            this.matchers = matchers;
        }
    }

    public InvocationMatcher(final Invocation invocation) {
        this(invocation, Collections.<Matcher>emptyList());
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

    @Override
    public String toString() {
        return new PrintSettings().print(matchers, invocation);
    }

    public boolean matches(final Invocation actual) {
        return invocation.getMock().equals(actual.getMock())
                && hasSameMethod(actual)
                && new ArgumentsComparator().argumentsMatch(this, actual);
    }

    private boolean safelyArgumentsMatch(final Object[] actualArgs) {
        try {
            return new ArgumentsComparator().argumentsMatch(this, actualArgs);
        } catch (final Throwable t) {
            return false;
        }
    }

    /**
     * similar means the same method name, same mock, unverified
     * and: if arguments are the same cannot be overloaded
     */
    public boolean hasSimilarMethod(final Invocation candidate) {
        final String wantedMethodName = getMethod().getName();
        final String currentMethodName = candidate.getMethod().getName();

        final boolean methodNameEquals = wantedMethodName.equals(currentMethodName);
        final boolean isUnverified = !candidate.isVerified();
        final boolean mockIsTheSame = getInvocation().getMock() == candidate.getMock();
        final boolean methodEquals = hasSameMethod(candidate);

        if (!methodNameEquals || !isUnverified || !mockIsTheSame) {
            return false;
        }

        final boolean overloadedButSameArgs = !methodEquals && safelyArgumentsMatch(candidate.getArguments());

        return !overloadedButSameArgs;
    }

    public boolean hasSameMethod(final Invocation candidate) {
        //not using method.equals() for 1 good reason:
        //sometimes java generates forwarding methods when generics are in play see JavaGenericsForwardingMethodsTest
        final Method m1 = invocation.getMethod();
        final Method m2 = candidate.getMethod();

        if (m1.getName() != null && m1.getName().equals(m2.getName())) {
            /* Avoid unnecessary cloning */
            final Class[] params1 = m1.getParameterTypes();
            final Class[] params2 = m2.getParameterTypes();
            if (params1.length == params2.length) {
                for (int i = 0; i < params1.length; i++) {
                    if (params1[i] != params2[i]) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public Location getLocation() {
        return invocation.getLocation();
    }

    public void captureArgumentsFrom(final Invocation invocation) {
        captureRegularArguments(invocation);
        captureVarargsPart(invocation);
    }

    private void captureRegularArguments(final Invocation invocation) {
        for (int position = 0; position < regularArgumentsSize(invocation); position++) {
            final Matcher m = matchers.get(position);
            if (m instanceof CapturesArguments) {
                ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
            }
        }
    }

    private void captureVarargsPart(final Invocation invocation) {
        if (!invocation.getMethod().isVarArgs()) {
            return;
        }
        final int indexOfVararg = invocation.getRawArguments().length - 1;
        for (final Matcher m : uniqueMatcherSet(indexOfVararg)) {
            if (m instanceof CapturesArguments) {
                final Object rawArgument = invocation.getRawArguments()[indexOfVararg];
                for (int i = 0; i < Array.getLength(rawArgument); i++) {
                    ((CapturesArguments) m).captureFrom(Array.get(rawArgument, i));
                }
            }
        }
    }

    private int regularArgumentsSize(final Invocation invocation) {
        return invocation.getMethod().isVarArgs()
                ? invocation.getRawArguments().length - 1 // ignores vararg holder array
                : matchers.size();
    }

    private Set<Matcher> uniqueMatcherSet(final int indexOfVararg) {
        final HashSet<Matcher> set = new HashSet<Matcher>();
        for (int position = indexOfVararg; position < matchers.size(); position++) {
            final Matcher matcher = matchers.get(position);
            if (matcher instanceof MatcherDecorator) {
                set.add(((MatcherDecorator) matcher).getActualMatcher());
            } else {
                set.add(matcher);
            }
        }
        return set;
    }

    public static List<InvocationMatcher> createFrom(final List<Invocation> invocations) {
        final LinkedList<InvocationMatcher> out = new LinkedList<InvocationMatcher>();
        for (final Invocation i : invocations) {
            out.add(new InvocationMatcher(i));
        }
        return out;
    }
}
