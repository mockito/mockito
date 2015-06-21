/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.mockito.MockitoMatcher;

import java.io.Serializable;

@SuppressWarnings("unchecked")
public class Not extends MockitoMatcher implements Serializable {

    private final MockitoMatcher first;

    public Not(MockitoMatcher first) {
        this.first = first;
    }

    public boolean matches(Object actual) {
        return !first.matches(actual);
    }

    public String describe() {
        return "not(" + first.describe() + ")";
    }
}