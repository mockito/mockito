/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;


public class StartsWith implements IArgumentMatcher<String> {

    private final String prefix;

    public StartsWith(String prefix) {
        this.prefix = prefix;
    }

    public boolean matches(String actual) {
        return actual != null && actual.startsWith(prefix);
    }

    public void appendTo(StringBuilder buffer) {
        buffer.append("startsWith(\"" + prefix + "\")");
    }
}
