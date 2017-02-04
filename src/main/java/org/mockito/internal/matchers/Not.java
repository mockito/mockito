/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;

import java.io.Serializable;

@SuppressWarnings({ "unchecked", "serial","rawtypes" })
public class Not implements ArgumentMatcher<Object>, Serializable {

    private final ArgumentMatcher matcher;

    public Not(ArgumentMatcher<?> matcher) {
        this.matcher = matcher;
    }

    public boolean matches(Object actual) {
        return !matcher.matches(actual);
    }

    public String toString() {
        return "not(" + matcher + ")";
    }
}
