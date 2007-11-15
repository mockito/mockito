/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal.matchers;

import java.util.regex.Pattern;

import org.easymock.IArgumentMatcher;

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
