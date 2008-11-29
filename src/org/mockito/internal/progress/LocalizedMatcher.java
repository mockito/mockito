package org.mockito.internal.progress;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.mockito.exceptions.base.MockitoException;

@SuppressWarnings("unchecked")
public class LocalizedMatcher extends BaseMatcher {

    private final Matcher matcher;
    private final MockitoException location;

    public LocalizedMatcher(Matcher matcher) {
        this.matcher = matcher;
        this.location = new MockitoException("Argument matcher was used here:");
    }

    public MockitoException getLocation() {
        return location;
    }

    public boolean matches(Object item) {
        return matcher.matches(item);
    }

    public void describeTo(Description description) {
        matcher.describeTo(description);
    }
}