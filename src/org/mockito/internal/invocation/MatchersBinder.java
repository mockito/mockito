/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.invocation;

import org.mockito.MockitoMatcher;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.matchers.LocalizedMatcher;
import org.mockito.internal.progress.ArgumentMatcherStorage;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.invocation.Invocation;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unchecked")
public class MatchersBinder implements Serializable {

    public InvocationMatcher bindMatchers(ArgumentMatcherStorage argumentMatcherStorage, Invocation invocation) {
        List<LocalizedMatcher> lastMatchers = argumentMatcherStorage.pullLocalizedMatchers();
        validateMatchers(invocation, lastMatchers);

        List<MockitoMatcher> matchers = new LinkedList<MockitoMatcher>();
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
                new Reporter().invalidUseOfMatchers(expectedMatchersSize, lastMatchers);
            }
        }
    }
}