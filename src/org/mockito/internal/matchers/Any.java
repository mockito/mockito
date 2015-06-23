/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import java.io.Serializable;

import org.mockito.ArgumentMatcher;

@SuppressWarnings("unchecked")
public class Any implements ArgumentMatcher, Serializable {

    public static final Any ANY = new Any();

    private Any() {
    }

    public boolean matches(Object actual) {
        return true;
    }

    public String toString() {
        return "<any>";
    }
}
