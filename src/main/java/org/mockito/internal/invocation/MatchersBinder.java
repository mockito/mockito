/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.invocation;


import org.mockito.ArgumentMatcher;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.internal.matchers.LocalizedMatcher;
import org.mockito.internal.matchers.MatcherMarkers;
import org.mockito.invocation.Invocation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.mockito.internal.exceptions.Reporter.invalidUseOfMatchers;

@SuppressWarnings("unchecked")
public class MatchersBinder implements Serializable {

    /**
     * Binds the given matchers with the given invocation and creates a new invocation-matcher.
     * <p>
     * The logic behind the binding is as follows:<ul>
     * <li>
     * In case no matchers are given, all of the invocation's arguments are converted to equals-matchers according
     * to their value, and these matchers are then bound with the invocation.</li>
     * <li>
     * In case at least one matcher is given, then it is assumed that the invocation's arguments consist of marker
     * values. Each non-marker argument is converted to an equals-matcher according to its value, while each marker
     * value is converted to the matcher next-in-line in the given matchers list. These converted matchers are then
     * bound with the invocation.
     * </li>
     * </ul>
     *
     * @param matchers   the given matchers; the only reason localized-matchers are required (and not simply
     *                   argument-matchers) is to have a better error reporting in case of an invalid use of this
     *                   method.
     * @param invocation the given invocation.
     * @return the newly created invocation-matcher.
     * @throws InvalidUseOfMatchersException if at least one matcher is given and the number of given matchers is not
     *                                       the same as the number of marker values in the arguments of the given
     *                                       invocation.
     * @see MatcherMarkers
     */
    public InvocationMatcher bindMatchers(List<LocalizedMatcher> matchers, Invocation invocation)
        throws InvalidUseOfMatchersException {

        Iterator<LocalizedMatcher> matchersIterator = matchers.iterator();
        Object[] arguments = invocation.getArguments();
        List<ArgumentMatcher> finalMatchers = new ArrayList<ArgumentMatcher>(arguments.length);

        for (Object argument : arguments) {
            if (!matchers.isEmpty() && MatcherMarkers.isMarker(argument)) {
                if (matchersIterator.hasNext()) {
                    finalMatchers.add(matchersIterator.next().getMatcher());
                } else {
                    throwInvalidUseOfMatchers(arguments, matchers);
                }
            } else {
                finalMatchers.add(ArgumentsProcessor.argumentToEqualsMatcher(argument));
            }
        }

        if (matchersIterator.hasNext()) {
            throwInvalidUseOfMatchers(arguments, matchers);
        }


        return new InvocationMatcher(invocation, finalMatchers);
    }

    private void throwInvalidUseOfMatchers(Object[] arguments, List<LocalizedMatcher> lastMatchers)
        throws InvalidUseOfMatchersException {

        int expectedMatchersSize = 0;
        for (Object argument : arguments) {
            if (MatcherMarkers.isMarker(argument)) {
                expectedMatchersSize += 1;
            }
        }
        throw invalidUseOfMatchers(expectedMatchersSize, lastMatchers);
    }
}
