/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.io.Serializable;

import org.mockito.ArgumentMatcher;
import org.mockito.CanIgnoreReturnValue;

public class Any implements ArgumentMatcher<Object>, Serializable {

    public static final Any ANY = new Any();

    @Override
    @CanIgnoreReturnValue
    public boolean matches(Object actual) {
        return true;
    }

    @Override
    public String toString() {
        return "<any>";
    }

    @Override
    public Class<?> type() {
        return Object.class;
    }
}
