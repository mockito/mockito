/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.util.ArrayList;
import java.util.List;

import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.IArgumentMatcher;
import org.mockito.internal.progress.LastArguments;

public class MatchersBinder {

    public InvocationMatcher bindMatchers(Invocation invocation) {
        List<IArgumentMatcher> lastMatchers = LastArguments.instance().pullMatchers();
        validateMatchers(invocation, lastMatchers);

        List<IArgumentMatcher> processedMatchers = createEqualsMatchers(invocation, lastMatchers);
        
        InvocationMatcher invocationWithMatchers = new InvocationMatcher(invocation, processedMatchers);
        return invocationWithMatchers;
    }

    private void validateMatchers(Invocation invocation, List<IArgumentMatcher> matchers) {
        if (matchers != null) {
            if (matchers.size() != invocation.getArguments().length) {
                throw new InvalidUseOfMatchersException(
                        invocation.getArguments().length
                        + " matchers expected, " + matchers.size()
                        + " recorded.");
            }
        }
    }
    
    /**
     * if user passed bare arguments then create EqualsMatcher for every argument.
     */
    private List<IArgumentMatcher> createEqualsMatchers(Invocation invocation,
            List<IArgumentMatcher> matchers) {
        if (matchers != null) {
            return matchers;
        }
        List<IArgumentMatcher> result = new ArrayList<IArgumentMatcher>();
        for (Object argument : invocation.getArguments()) {
            result.add(new Equals(argument));
        }
        return result;
    }
}
