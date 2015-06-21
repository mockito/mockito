/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.invocation;

import org.mockito.MockitoMatcher;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.matchers.LocalizedMatcher;
import org.mockito.internal.progress.ArgumentMatcherStorage;
import org.mockito.invocation.Invocation;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("unchecked")
public class MatchersBinder implements Serializable {

    public InvocationMatcher bindMatchers(ArgumentMatcherStorage argumentMatcherStorage, Invocation invocation) {
        List<LocalizedMatcher> lastMatchers = argumentMatcherStorage.pullLocalizedMatchers();
        validateMatchers(invocation, lastMatchers);

        InvocationMatcher invocationWithMatchers = new InvocationMatcher(invocation, (List<MockitoMatcher>)(List) lastMatchers);
        return invocationWithMatchers;
    }

    private void validateMatchers(Invocation invocation, List<LocalizedMatcher> lastMatchers) {
        if (!lastMatchers.isEmpty()) {
            int recordedMatchersSize = lastMatchers.size();
            int expectedMatchersSize = invocation.getArguments().length;
            if (expectedMatchersSize != recordedMatchersSize) {
                new Reporter().invalidUseOfMatchers(expectedMatchersSize, lastMatchers);
            }
        }
    }
}