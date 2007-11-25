/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.util.regex.Pattern;


public class Find implements IArgumentMatcher {

    private final String regex;

    public Find(String regex) {
        this.regex = regex;
    }

    public boolean matches(Object actual) {
        return (actual instanceof String)
                && Pattern.compile(regex).matcher((String) actual).find();
    }

    public void appendTo(StringBuffer buffer) {
        buffer.append("find(\"" + regex.replaceAll("\\\\", "\\\\\\\\") + "\")");
    }
}
