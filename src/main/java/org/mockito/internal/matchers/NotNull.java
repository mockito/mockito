/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.io.Serializable;

import org.mockito.ArgumentMatcher;

public class NotNull implements ArgumentMatcher<Object>, Serializable {

    public static final NotNull NOT_NULL = new NotNull();

    private NotNull() {}

    @Override
    public boolean matches(Object actual) {
        return actual != null;
    }

    @Override
    public String toString() {
        return "notNull()";
    }
}
