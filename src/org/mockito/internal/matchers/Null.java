/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;


public class Null implements ArgumentMatcher<Object> {

    public static final Null NULL = new Null();

    private Null() {
    }

    public boolean matches(Object actual) {
        return actual == null;
    }

    public void appendTo(StringBuilder buffer) {
        buffer.append("isNull()");
    }
}
