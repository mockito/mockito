/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.mockito.MockitoMatcher;

import java.io.Serializable;


public class InstanceOf extends MockitoMatcher<Object> implements Serializable {

    private final Class<?> clazz;

    public InstanceOf(Class<?> clazz) {
        this.clazz = clazz;
    }

    public boolean matches(Object actual) {
        return (actual != null) && clazz.isAssignableFrom(actual.getClass());
    }

    public String describe() {
        return "isA(" + clazz.getName() + ")";
    }
}
