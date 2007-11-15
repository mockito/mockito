/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal.matchers;

import org.easymock.IArgumentMatcher;

public class Matches implements IArgumentMatcher {

    private final String regex;

    public Matches(String regex) {
        this.regex = regex;
    }

    public boolean matches(Object actual) {
        return (actual instanceof String) && ((String) actual).matches(regex);
    }

    public void appendTo(StringBuffer buffer) {
        buffer.append("matches(\"" + regex.replaceAll("\\\\", "\\\\\\\\")
                + "\")");
    }
}
