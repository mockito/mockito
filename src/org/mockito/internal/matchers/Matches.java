/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;


public class Matches extends ArgumentMatcher<Object> {

    private final String regex;

    public Matches(String regex) {
        this.regex = regex;
    }

    public boolean matches(Object actual) {
        return (actual instanceof String) && ((String) actual).matches(regex);
    }

    public void describeTo(Description description) {
        description.appendText("matches(\"" + regex.replaceAll("\\\\", "\\\\\\\\")
                + "\")");
    }
}
