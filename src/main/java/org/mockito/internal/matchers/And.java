/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import java.io.Serializable;

import org.mockito.ArgumentMatcher;

@SuppressWarnings({ "unchecked", "serial","rawtypes" })
public class And implements ArgumentMatcher<Object>, Serializable {
    private ArgumentMatcher m1;
    private ArgumentMatcher m2;

    public And(ArgumentMatcher<?> m1, ArgumentMatcher<?> m2) {
        this.m1 = m1;
        this.m2 = m2;
    }

    public boolean matches(Object actual) {
        return m1.matches(actual) && m2.matches(actual);
    }

    public String toString() {
        return "and("+m1+", "+m2+")";
    }
}
