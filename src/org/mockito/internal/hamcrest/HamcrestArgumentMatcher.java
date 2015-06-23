package org.mockito.internal.hamcrest;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.mockito.MockitoMatcher;

/**
 * Created by sfaber on 6/22/15.
 */
public class HamcrestArgumentMatcher<T> implements MockitoMatcher<T> {

    private final Matcher matcher;

    public <T> HamcrestArgumentMatcher(Matcher<T> matcher) {
        this.matcher = matcher;
    }

    public boolean matches(Object argument) {
        return this.matcher.matches(argument);
    }

    public String toString() {
        //TODO SF add unit tests and integ test coverage for describeTo()
        StringDescription s = new StringDescription();
        this.matcher.describeTo(s);
        return s.toString();
    }
}