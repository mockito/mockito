/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.io.Serializable;
import java.util.regex.Pattern;

import org.mockito.ArgumentMatcher;

public class Find implements ArgumentMatcher<String>, Serializable {

    private final String regex;

    public Find(String regex) {
        this.regex = regex;
    }

    @Override
    public boolean matches(String actual) {
        return actual != null && Pattern.compile(regex).matcher(actual).find();
    }

    @Override
    public String toString() {
        return "find(\"" + regex.replace("\\", "\\\\") + "\")";
    }
}
