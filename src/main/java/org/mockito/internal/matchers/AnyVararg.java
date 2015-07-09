/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;

import java.io.Serializable;

@SuppressWarnings("unchecked")
public class AnyVararg implements ArgumentMatcher, VarargMatcher, Serializable {

    public static final ArgumentMatcher ANY_VARARG = new AnyVararg();

    public boolean matches(Object arg) {
        return true;
    }

    public String toString() {
        return "<any>";
    }
}