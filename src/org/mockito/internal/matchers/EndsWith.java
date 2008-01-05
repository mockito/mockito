/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;


public class EndsWith implements IArgumentMatcher<String> {

    private final String suffix;

    public EndsWith(String suffix) {
        this.suffix = suffix;
    }

    public boolean matches(String actual) {
        return actual != null && actual.endsWith(suffix);
    }

    public void appendTo(StringBuilder buffer) {
        buffer.append("endsWith(\"" + suffix + "\")");
    }
}