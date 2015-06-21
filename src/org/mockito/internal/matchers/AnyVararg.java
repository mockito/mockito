/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.mockito.MockitoMatcher;

import java.io.Serializable;

@SuppressWarnings("unchecked")
public class AnyVararg extends MockitoMatcher implements VarargMatcher, Serializable {

    public static final MockitoMatcher ANY_VARARG = new AnyVararg();

    public boolean matches(Object arg) {
        return true;
    }

    public String describe() {
        return "<any>";
    }
}