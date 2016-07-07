package org.mockito.internal.hamcrest;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.mockito.ArgumentMatcher;

public class HamcrestArgumentMatcher<T> implements ArgumentMatcher<T> {

    private final Matcher matcher;

    public HamcrestArgumentMatcher(Matcher<T> matcher) {
        this.matcher = matcher;
    }

    public boolean matches(Object argument) {
        return this.matcher.matches(argument);
    }

    public String toString() {
        //TODO SF add unit tests and integ test coverage for describeTo()
        return StringDescription.toString(matcher);
    }
}