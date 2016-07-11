/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.invocation;


import static org.mockito.internal.exceptions.Reporter.invalidUseOfMatchers;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.LocalizedMatcher;
import org.mockito.internal.progress.ArgumentMatcherStorage;
import org.mockito.invocation.Invocation;

@SuppressWarnings("unchecked")
public class MatchersBinder implements Serializable {

    public InvocationMatcher bindMatchers(ArgumentMatcherStorage argumentMatcherStorage, Invocation invocation) {
        List<LocalizedMatcher> lastMatchers = argumentMatcherStorage.pullLocalizedMatchers();
        validateMatchers(invocation, lastMatchers);

        List<ArgumentMatcher> matchers = new LinkedList<ArgumentMatcher>();
        for (LocalizedMatcher m : lastMatchers) {
            matchers.add(m.getMatcher());
        }
        return new InvocationMatcher(invocation, matchers);
    }

    private void validateMatchers(Invocation invocation, List<LocalizedMatcher> lastMatchers) {
        if (!lastMatchers.isEmpty()) {
            int recordedMatchersSize = lastMatchers.size();
            int expectedMatchersSize = invocation.getArguments().length;
            if (expectedMatchersSize != recordedMatchersSize) {
                throw invalidUseOfMatchers(expectedMatchersSize, lastMatchers);
            }
        }
    }
}
