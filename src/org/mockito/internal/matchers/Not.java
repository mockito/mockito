/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;


public class Not implements IArgumentMatcher {

    private final IArgumentMatcher first;

    public Not(IArgumentMatcher first) {
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
