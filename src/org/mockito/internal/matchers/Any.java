/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import java.io.Serializable;

import org.mockito.MockitoMatcher;

@SuppressWarnings("unchecked")
public class Any extends MockitoMatcher implements Serializable {

    public static final Any ANY = new Any();

    private Any() {
    }

    public boolean matches(Object actual) {
        return true;
    }

    public String describe() {
        return "<any>";
    }
}
