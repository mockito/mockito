/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.matchers.LocalizedMatcher;
import org.mockito.internal.progress.ArgumentMatcherStorage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class MatchersBinder implements Serializable {

    private static final long serialVersionUID = -311433939339443463L;

    public InvocationMatcher bindMatchers(ArgumentMatcherStorage argumentMatcherStorage, Invocation invocation) {
        List<Matcher> lastMatchers = argumentMatcherStorage.pullMatchers();
        validateMatchers(invocation, lastMatchers);

        InvocationMatcher invocationWithMatchers = new InvocationMatcher(invocation, lastMatchers);
        return invocationWithMatchers;
    }

    private void validateMatchers(Invocation invocation, List<? extends Matcher> matchers) {
        if (!matchers.isEmpty()) {
            int recordedMatchersSize = matchers.size();
            int expectedMatchersSize = invocation.getArgumentsCount();
            if (expectedMatchersSize != recordedMatchersSize) {
                List<LocalizedMatcher> lastMatchers = new ArrayList<LocalizedMatcher>((List<LocalizedMatcher>) matchers);
                new Reporter().invalidUseOfMatchers(expectedMatchersSize, lastMatchers);
            }
        }
    }
}