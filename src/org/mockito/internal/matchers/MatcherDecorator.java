package org.mockito.internal.matchers;

import org.hamcrest.Matcher;

public interface MatcherDecorator {
    Matcher getActualMatcher();
}
