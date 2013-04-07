/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.invocation.Invocation;

import java.util.List;

@SuppressWarnings("unchecked")
public class ArgumentsComparator {
    public boolean argumentsMatch(InvocationMatcher invocationMatcher, Invocation actual) {
        Object[] actualArgs = actual.getArguments();
        return argumentsMatch(invocationMatcher, actualArgs) || varArgsMatch(invocationMatcher, actual);
    }

    public boolean argumentsMatch(InvocationMatcher invocationMatcher, Object[] actualArgs) {
        if (actualArgs.length != invocationMatcher.getMatchers().size()) {
            return false;
        }
        for (int i = 0; i < actualArgs.length; i++) {
            if (!invocationMatcher.getMatchers().get(i).matches(actualArgs[i])) {
                return false;
            }
        }
        return true;
    }

    //ok, this method is a little bit messy but the vararg business unfortunately is messy...      
    private boolean varArgsMatch(InvocationMatcher invocationMatcher, Invocation actual) {
        if (!actual.getMethod().isVarArgs()) {
            //if the method is not vararg forget about it
            return false;
        }

        //we must use raw arguments, not arguments...
        Object[] rawArgs = actual.getRawArguments();
        List<Matcher> matchers = invocationMatcher.getMatchers();

        if (rawArgs.length != matchers.size()) {
            return false;
        }

        for (int i = 0; i < rawArgs.length; i++) {
            Matcher m = matchers.get(i);
            //it's a vararg because it's the last array in the arg list
            if (rawArgs[i] != null && rawArgs[i].getClass().isArray() && i == rawArgs.length-1) {
                Matcher actualMatcher;
                //this is necessary as the framework often decorates matchers
                if (m instanceof MatcherDecorator) {
                    actualMatcher = ((MatcherDecorator)m).getActualMatcher();
                } else {
                    actualMatcher = m;
                }
                //this is very important to only allow VarargMatchers here. If you're not sure why remove it and run all tests.
                if (!(actualMatcher instanceof VarargMatcher) || !actualMatcher.matches(rawArgs[i])) {
                    return false;
                }
            //it's not a vararg (i.e. some ordinary argument before varargs), just do the ordinary check
            } else if (!m.matches(rawArgs[i])){
                return false;
            }
        }

        return true;
    }
}