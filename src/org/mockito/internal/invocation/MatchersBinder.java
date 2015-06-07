/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.invocation;

import java.io.Serializable;
import java.util.List;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.matchers.LocalizedMatcher;
import org.mockito.internal.progress.ArgumentMatcherStorage;
import org.mockito.invocation.Invocation;

@SuppressWarnings("unchecked")
public class MatchersBinder implements Serializable {

    private static final long serialVersionUID = -311433939339443463L;

    public InvocationMatcher bindMatchers(final ArgumentMatcherStorage argumentMatcherStorage, final Invocation invocation) {
        final List<LocalizedMatcher> lastMatchers = argumentMatcherStorage.pullLocalizedMatchers();
        validateMatchers(invocation, lastMatchers);

        final InvocationMatcher invocationWithMatchers = new InvocationMatcher(invocation, (List) lastMatchers);
        return invocationWithMatchers;
    }

    private void validateMatchers(final Invocation invocation, final List<LocalizedMatcher> lastMatchers) {
        if (!lastMatchers.isEmpty()) {
            final int recordedMatchersSize = lastMatchers.size();
            final int expectedMatchersSize = invocation.getArguments().length;
            if (expectedMatchersSize != recordedMatchersSize) {
                new Reporter().invalidUseOfMatchers(expectedMatchersSize, lastMatchers);
            }
        }
    }
}