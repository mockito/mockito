/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.invocation;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings("unchecked")
public class InvocationMatcher implements DescribedInvocation, CapturesArgumentsFromInvocation, Serializable {

    private final Invocation invocation;
    private final List<ArgumentMatcher> matchers;

    public InvocationMatcher(Invocation invocation, List<ArgumentMatcher> matchers) {
        this.invocation = invocation;
        if (matchers.isEmpty()) {
            this.matchers = ArgumentsProcessor.argumentsToMatchers(invocation.getArguments());
        } else {
            this.matchers = matchers;
        }
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

    public String toString() {
        return new PrintSettings().print(matchers, invocation);
    }

    public boolean matches(Invocation actual) {
        return invocation.getMock().equals(actual.getMock())
                && hasSameMethod(actual)
                && new ArgumentsComparator().argumentsMatch(this, actual);
    }

    private boolean safelyArgumentsMatch(Object[] actualArgs) {
        try {
            return new ArgumentsComparator().argumentsMatch(this, actualArgs);
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * similar means the same method name, same mock, unverified
     * and: if arguments are the same cannot be overloaded
     */
    public boolean hasSimilarMethod(Invocation candidate) {
        String wantedMethodName = getMethod().getName();
        String currentMethodName = candidate.getMethod().getName();

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

    public boolean hasSameMethod(Invocation candidate) {
        //not using method.equals() for 1 good reason:
        //sometimes java generates forwarding methods when generics are in play see JavaGenericsForwardingMethodsTest
        Method m1 = invocation.getMethod();
        Method m2 = candidate.getMethod();

        if (m1.getName() != null && m1.getName().equals(m2.getName())) {
            /* Avoid unnecessary cloning */
            Class[] params1 = m1.getParameterTypes();
            Class[] params2 = m2.getParameterTypes();
            if (params1.length == params2.length) {
                for (int i = 0; i < params1.length; i++) {
                    if (params1[i] != params2[i])
                        return false;
                }
                return true;
            }
        }
        return false;
    }

    public Location getLocation() {
        return invocation.getLocation();
    }

    public void captureArgumentsFrom(Invocation invocation) {
        captureRegularArguments(invocation);
        captureVarargsPart(invocation);
    }

    private void captureRegularArguments(Invocation invocation) {
        for (int position = 0; position < regularArgumentsSize(invocation); position++) {
            ArgumentMatcher m = matchers.get(position);
            if (m instanceof CapturesArguments) {
                ((CapturesArguments) m).captureFrom(invocation.getArgumentAt(position, Object.class));
            }
        }
    }

    private void captureVarargsPart(Invocation invocation) {
        if (!invocation.getMethod().isVarArgs()) {
            return;
        }
        int indexOfVararg = invocation.getRawArguments().length - 1;
        for (ArgumentMatcher m : uniqueMatcherSet(indexOfVararg)) {
            if (m instanceof CapturesArguments) {
                Object rawArgument = invocation.getRawArguments()[indexOfVararg];
                for (int i = 0; i < Array.getLength(rawArgument); i++) {
                    ((CapturesArguments) m).captureFrom(Array.get(rawArgument, i));
                }
            }
        }
    }

    private int regularArgumentsSize(Invocation invocation) {
        return invocation.getMethod().isVarArgs() ?
                invocation.getRawArguments().length - 1 // ignores vararg holder array
                : matchers.size();
    }

    private Set<ArgumentMatcher> uniqueMatcherSet(int indexOfVararg) {
        HashSet<ArgumentMatcher> set = new HashSet<ArgumentMatcher>();
        for (int position = indexOfVararg; position < matchers.size(); position++) {
            ArgumentMatcher matcher = matchers.get(position);
            set.add(matcher);
        }
        return set;
    }

    public static List<InvocationMatcher> createFrom(List<Invocation> invocations) {
        LinkedList<InvocationMatcher> out = new LinkedList<InvocationMatcher>();
        for (Invocation i : invocations) {
            out.add(new InvocationMatcher(i));
        }
        return out;
    }
}
