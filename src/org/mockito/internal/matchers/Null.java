/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;


public class Null implements IArgumentMatcher {

    public static final Null NULL = new Null();

    private Null() {
    }

    public boolean matches(Object actual) {
        return actual == null;
    }

    public void appendTo(StringBuffer buffer) {
        buffer.append("isNull()");
    }
}
