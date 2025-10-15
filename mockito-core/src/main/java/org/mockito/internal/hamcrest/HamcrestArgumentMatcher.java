/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.hamcrest;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.mockito.ArgumentMatcher;

import static java.util.Objects.requireNonNull;

public class HamcrestArgumentMatcher<T> implements ArgumentMatcher<T> {

    private final Matcher<T> matcher;
    private final Class<?> type;

    public HamcrestArgumentMatcher(Matcher<T> matcher) {
        this(Void.class, matcher);
    }

    public HamcrestArgumentMatcher(Matcher<T> matcher, Class<T> type) {
        this(type, matcher);
    }

    private HamcrestArgumentMatcher(Class<?> type, Matcher<T> matcher) {
        this.type = requireNonNull(type, "type");
        this.matcher = requireNonNull(matcher, "matcher");
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

    @Override
    public Class<?> type() {
        return type;
    }
}
