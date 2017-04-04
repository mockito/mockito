/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.migration;

import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.MatchersPrinter;
import org.mockito.internal.reporting.PrintSettings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MockitoV1CompatibilityMatcherTest {

    @Test
    public void testToString_AnonymousClass() {
        ArgumentMatcher<Integer> matcher = new MockitoV1CompatibilityMatcher<Integer>() {
            @Override
            public boolean matchesSafely(Integer argument) {
                return true;
            }
        };

        assertEquals("(<custom argument matcher>);", matcherToString(matcher));
        assertTrue(matcher.matches(1));
        assertFalse(matcher.matches(1L));
    }

    @SuppressWarnings("unchecked")
    private String matcherToString(ArgumentMatcher<Integer> matcher) {
        return new MatchersPrinter().getArgumentsLine((List) Collections.singletonList(matcher),
            new PrintSettings());
    }

    @Test
    public void testToString_NestedClass() {
        ArgumentMatcher<Integer> matcher = new NoIntegerMatching();

        assertEquals("(<No integer matching>);", matcherToString(matcher));
        assertTrue(matcher.matches(1));
        assertFalse(matcher.matches(1L));
    }

    private static class NoIntegerMatching extends MockitoV1CompatibilityMatcher<Integer> {

        @Override
        public boolean matchesSafely(Integer argument) {
            return true;
        }
    }
}
