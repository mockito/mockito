/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.invocation;


import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.LocalizedMatcher;
import org.mockito.internal.progress.ArgumentMatcherStorage;
import org.mockito.invocation.Invocation;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.internal.exceptions.Reporter.invalidUseOfMatchers;
import static org.mockito.internal.invocation.ArgumentsProcessor.argumentToMatcher;
import static org.mockito.internal.invocation.ArgumentsProcessor.argumentsToMatchers;
import static org.mockito.internal.invocation.MatchersBinder.MatcherReturnValues.INT_MARKER;

@SuppressWarnings("unchecked")
public class MatchersBinder implements Serializable {

    public InvocationMatcher bindMatchers(ArgumentMatcherStorage argumentMatcherStorage, Invocation invocation) {
        List<LocalizedMatcher> lastMatchers = argumentMatcherStorage.pullLocalizedMatchers();

        List<ArgumentMatcher> matchers = toMatchers(invocation, lastMatchers);

        return new InvocationMatcher(invocation, matchers);
    }

    private List<ArgumentMatcher> toMatchers(Invocation invocation, List<LocalizedMatcher> localizedMatchers) {
        LinkedList<ArgumentMatcher> matchers = toMatchers(localizedMatchers);


        int matcherCount = matchers.size();
        int argumentCount = invocation.getArguments().length;

        if (matcherCount == 0) {
            return argumentsToMatchers(invocation.getArguments());
        }

        if (matcherCount == argumentCount) {
            return matchers;
        }

        return completeMatchers(invocation, matchers, localizedMatchers);


    }

    private List<ArgumentMatcher> completeMatchers(Invocation invocation, LinkedList<ArgumentMatcher> matchers, List<LocalizedMatcher> localizedMatchers) {
        List<ArgumentMatcher> completedMatchers = new LinkedList<ArgumentMatcher>();
        Class<?>[] paramTypes = invocation.getMethod().getParameterTypes();
        Object[] paramValues = invocation.getRawArguments();

        ArgumentMatcher<?> currentMatcher = matchers.pollFirst();

        for (int i = 0; i < paramTypes.length; i++) {


            Class<?> currentParamType = paramTypes[i];
            Object curentParamValue = paramValues[i];

            if (currentParamType == int.class && (Integer) curentParamValue != INT_MARKER) {
                completedMatchers.add(argumentToMatcher(curentParamValue));
            } else {
                if (currentMatcher == null) {
                    //more arguments that matchers found
                    throw invalidUseOfMatchers(invocation.getArguments().length, localizedMatchers);
                }
                completedMatchers.add(currentMatcher);
                currentMatcher = matchers.pollFirst();
            }

        }

        if (currentMatcher != null) { //all matchers have be consumed, otherwise too many matchers were specified
            throw invalidUseOfMatchers(invocation.getArguments().length, localizedMatchers);
        }

        return completedMatchers;
    }


    private LinkedList<ArgumentMatcher> toMatchers(List<LocalizedMatcher> lastMatchers) {
        LinkedList<ArgumentMatcher> matchers = new LinkedList<ArgumentMatcher>();
        for (LocalizedMatcher m : lastMatchers) {
            matchers.add(m.getMatcher());
        }
        return matchers;
    }


    public static class MatcherReturnValues {
        public static int INT_MARKER = 0; //Integer.MIN_VALUE+8;
    }
}
