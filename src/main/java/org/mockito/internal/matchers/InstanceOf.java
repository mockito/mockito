/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;

import java.io.Serializable;


public class InstanceOf implements ArgumentMatcher<Object>, Serializable {

    private final Class<?> clazz;

    public InstanceOf(Class<?> clazz) {
        this.clazz = clazz;
    }

    public boolean matches(Object actual) {
        return (actual != null) && clazz.isAssignableFrom(actual.getClass());
    }

    public String toString() {
        return "isA(" + clazz.getName() + ")";
    }
}
