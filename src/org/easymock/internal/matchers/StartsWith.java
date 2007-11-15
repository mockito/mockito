/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal.matchers;

import org.easymock.IArgumentMatcher;

public class StartsWith implements IArgumentMatcher {

    private final String prefix;

    public StartsWith(String prefix) {
        this.prefix = prefix;
    }

    public boolean matches(Object actual) {
        return (actual instanceof String)
                && ((String) actual).startsWith(prefix);
    }

    public void appendTo(StringBuffer buffer) {
        buffer.append("startsWith(\"" + prefix + "\")");
    }
}
