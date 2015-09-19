/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;

import java.io.Serializable;

public class NotNull implements ArgumentMatcher<Object>, Serializable {

    public static final NotNull NOT_NULL = new NotNull();

    private NotNull() {
    }

    public boolean matches(Object actual) {
        return actual != null;
    }

    public String toString() {
        return "notNull()";
    }
}
