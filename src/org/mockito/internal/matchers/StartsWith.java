/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;


public class StartsWith implements IArgumentMatcher {

    private final String prefix;

    public StartsWith(String prefix) {
        this.prefix = prefix;
    }

    public boolean matches(Object actual) {
        return (actual instanceof String)
                && ((String) actual).startsWith(prefix);
    }

    public void appendTo(StringBuilder buffer) {
        buffer.append("startsWith(\"" + prefix + "\")");
    }
}
