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
public class InvocationMatcher implements PrintableInvocation, CanPrintInMultilines {

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
    
    /* (non-Javadoc)
     * @see org.mockito.internal.invocation.CanPrintInMultilines#toString()
     */
    public String toString() {
        return invocation.toString(matchers, false);
    }

    /* (non-Javadoc)
     * @see org.mockito.internal.invocation.CanPrintInMultilines#hasMultilinePrint()
     */
    public boolean printsInMultilines() {        
        return toString().contains("\n");
    }

    /* (non-Javadoc)
     * @see org.mockito.internal.invocation.CanPrintInMultilines#toMultilineString()
     */
    public String toMultilineString() {
        return invocation.toString(matchers, true);
    }    

    public boolean matches(Invocation actual) {
        return invocation.getMock().equals(actual.getMock())
                && hasSameMethod(actual)
                && argumentsMatch(actual);
    }

    private boolean argumentsMatch(Invocation actual) {
        Object[] arguments = actual.getArguments();
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
        final boolean overloadedButSameArgs = !methodEquals && argumentsMatch(candidate);        
        
        if (methodNameEquals && isUnverified && mockIsTheSame && !overloadedButSameArgs) {
            return true;
        }
        
        return false;
    }

    public boolean hasSameMethod(Invocation candidate) {
        return invocation.getMethod().equals(candidate.getMethod());
    }
}