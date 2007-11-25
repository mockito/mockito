/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;


public class Same implements IArgumentMatcher {

    private final Object expected;

    public Same(Object expected) {
        this.expected = expected;
    }

    public boolean matches(Object actual) {
        return expected == actual;
    }

    public void appendTo(StringBuffer buffer) {
        buffer.append("same(");
        appendQuoting(buffer);
        buffer.append(expected);
        appendQuoting(buffer);
        buffer.append(")");
    }

    private void appendQuoting(StringBuffer buffer) {
        if (expected instanceof String) {
            buffer.append("\"");
        } else if (expected instanceof Character) {
            buffer.append("'");
        }
    }
}
