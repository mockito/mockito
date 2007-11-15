package org.mockito.matchers;

import org.hamcrest.Matcher;

public interface MockitoMatcher<T> extends Matcher<T> {
    T getMock();
}
