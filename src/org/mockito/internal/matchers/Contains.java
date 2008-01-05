/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;


public class Contains implements IArgumentMatcher<String> {

    private final String substring;

    public Contains(String substring) {
        this.substring = substring;
    }

    public boolean matches(String actual) {
        return actual != null && actual.contains(substring);
    }

    public void appendTo(StringBuilder buffer) {
        buffer.append("contains(\"" + substring + "\")");
    }
}
