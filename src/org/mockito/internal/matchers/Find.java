/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.util.regex.Pattern;

public class Find implements ArgumentMatcher<String> {

    private final String regex;

    public Find(String regex) {
        this.regex = regex;
    }

    public boolean matches(String actual) {
        return actual != null && Pattern.compile(regex).matcher(actual).find();
    }

    public void appendTo(StringBuilder buffer) {
        buffer.append("find(\"" + regex.replaceAll("\\\\", "\\\\\\\\") + "\")");
    }
}