/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

@SuppressWarnings("unchecked")
public class Not implements ArgumentMatcher {

    private final ArgumentMatcher first;

    public Not(ArgumentMatcher first) {
        this.first = first;
    }

    public boolean matches(Object actual) {
        return !first.matches(actual);
    }

    public void appendTo(StringBuilder buffer) {
        buffer.append("not(");
        first.appendTo(buffer);
        buffer.append(")");
    }
}
