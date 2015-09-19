/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import org.mockito.ArgumentMatcher;
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
        List<ArgumentMatcher> matchers = invocationMatcher.getMatchers();

        if (rawArgs.length != matchers.size()) {
            return false;
        }

        for (int i = 0; i < rawArgs.length; i++) {
            ArgumentMatcher m = matchers.get(i);
            //it's a vararg because it's the last array in the arg list
            if (rawArgs[i] != null && rawArgs[i].getClass().isArray() && i == rawArgs.length-1) {
                //this is very important to only allow VarargMatchers here. If you're not sure why remove it and run all tests.
                if (!(m instanceof VarargMatcher) || !m.matches(rawArgs[i])) {
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