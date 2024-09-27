/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers.text;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockitoutil.TestBase;

public class MatcherToStringTest extends TestBase {

    static class MatcherWithoutDescription implements ArgumentMatcher<Object> {
        @Override
        public boolean matches(Object argument) {
            return false;
        }
    }

    static class MatcherWithDescription implements ArgumentMatcher<Object> {
        @Override
        public boolean matches(Object argument) {
            return false;
        }

        @Override
        public String toString() {
            return "*my custom description*";
        }
    }

    static class MatcherWithInheritedDescription extends MatcherWithDescription {
        @Override
        public boolean matches(Object argument) {
            return false;
        }
    }

    @Test
    public void better_toString_for_matchers() {
        assertEquals(
                "<Matcher without description>",
                MatcherToString.toString(new MatcherWithoutDescription()));
        assertEquals(
                "*my custom description*", MatcherToString.toString(new MatcherWithDescription()));
        assertEquals(
                "*my custom description*",
                MatcherToString.toString(new MatcherWithInheritedDescription()));
    }

    @Test
    public void default_name_for_anonymous_matchers() {
        ArgumentMatcher<Object> anonymousMatcher =
                new ArgumentMatcher<Object>() {
                    @Override
                    public boolean matches(Object argument) {
                        return false;
                    }
                };
        assertEquals("<custom argument matcher>", MatcherToString.toString(anonymousMatcher));

        ArgumentMatcher<Object> anonymousDescriptiveMatcher =
                new MatcherWithDescription() {
                    @Override
                    public boolean matches(Object argument) {
                        return false;
                    }
                };
        assertEquals(
                "*my custom description*", MatcherToString.toString(anonymousDescriptiveMatcher));
    }

    @Test
    public void default_name_for_synthetic_matchers() {
        ArgumentMatcher<Object> lambdaMatcher = argument -> true;
        assertEquals("<custom argument matcher>", MatcherToString.toString(lambdaMatcher));

        ArgumentMatcher<Object> methodRefMatcher = lambdaMatcher::matches;
        assertEquals("<custom argument matcher>", MatcherToString.toString(methodRefMatcher));
    }
}
