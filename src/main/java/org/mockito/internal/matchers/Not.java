/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;

import java.io.Serializable;

@SuppressWarnings("unchecked")
public class Not implements ArgumentMatcher, Serializable {

    private final ArgumentMatcher first;

    public Not(ArgumentMatcher first) {
        this.first = first;
    }

    public boolean matches(Object actual) {
        return !first.matches(actual);
    }

    public String toString() {
        return "not(" + first.toString() + ")";
    }
}