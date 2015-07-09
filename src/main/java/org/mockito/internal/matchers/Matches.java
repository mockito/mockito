/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;

import java.io.Serializable;

public class Matches implements ArgumentMatcher<Object>, Serializable {

    private final String regex;

    public Matches(String regex) {
        this.regex = regex;
    }

    public boolean matches(Object actual) {
        return (actual instanceof String) && ((String) actual).matches(regex);
    }

    public String toString() {
        return "matches(\"" + regex.replaceAll("\\\\", "\\\\\\\\") + "\")";
    }
}