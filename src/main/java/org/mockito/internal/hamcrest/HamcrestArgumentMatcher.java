/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.hamcrest;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.mockito.ArgumentMatcher;

public class HamcrestArgumentMatcher<T> implements ArgumentMatcher<T> {

    private final Matcher<T> matcher;

    public HamcrestArgumentMatcher(Matcher<T> matcher) {
        this.matcher = matcher;
    }

    @Override
    public boolean matches(Object argument) {
        return this.matcher.matches(argument);
    }

    @Override
    public String toString() {
        // TODO SF add unit tests and integ test coverage for toString()
        return StringDescription.toString(matcher);
    }
}
