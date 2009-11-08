package org.mockito.internal.matchers;

import java.io.Serializable;

import org.hamcrest.Matcher;

@SuppressWarnings("unchecked")
public interface MatcherDecorator extends Serializable {
    Matcher getActualMatcher();
}
