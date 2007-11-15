/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal.matchers;

import org.easymock.IArgumentMatcher;

public class Not implements IArgumentMatcher {

    private final IArgumentMatcher first;

    public Not(IArgumentMatcher first) {
        this.first = first;
    }

    public boolean matches(Object actual) {
        return !first.matches(actual);
    }

    public void appendTo(StringBuffer buffer) {
        buffer.append("not(");
        first.appendTo(buffer);
        buffer.append(")");
    }
}
