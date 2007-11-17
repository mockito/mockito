/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;


public class Contains implements IArgumentMatcher {

    private final String substring;

    public Contains(String substring) {
        this.substring = substring;
    }

    public boolean matches(Object actual) {
        return (actual instanceof String)
                && ((String) actual).indexOf(substring) >= 0;
    }

    public void appendTo(StringBuffer buffer) {
        buffer.append("contains(\"" + substring + "\")");
    }
}
