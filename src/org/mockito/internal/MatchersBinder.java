package org.mockito.internal;

import java.util.*;

import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.internal.matchers.*;

public class MatchersBinder {

    public InvocationMatcher bindMatchers(Invocation invocation) throws InvalidUseOfMatchersException {
        List<IArgumentMatcher> lastMatchers = LastArguments.instance().pullMatchers();
        validateMatchers(invocation, lastMatchers);

        List<IArgumentMatcher> processedMatchers = createEqualsMatchers(invocation, lastMatchers);
        
        InvocationMatcher invocationWithMatchers = new InvocationMatcher(invocation, processedMatchers);
        return invocationWithMatchers;
    }

    private void validateMatchers(Invocation invocation, List<IArgumentMatcher> matchers) throws InvalidUseOfMatchersException {
        if (matchers != null) {
            if (matchers.size() != invocation.getArguments().length) {
                throw new InvalidUseOfMatchersException(
                        + invocation.getArguments().length
                        + " matchers expected, " + matchers.size()
                        + " recorded.");
            }
        }
    }
    
    /**
     * if user passed bare arguments then create EqualsMatcher for every argument
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
