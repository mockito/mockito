/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.util.Primitives;

import java.io.Serializable;


public class InstanceOf implements ArgumentMatcher<Object>, Serializable {

    private final Class<?> clazz;
    private String description;

    public InstanceOf(Class<?> clazz) {
        this(clazz, "isA(" + clazz.getCanonicalName() + ")");
    }

    public InstanceOf(Class<?> clazz, String describedAs) {
        this.clazz = clazz;
        this.description = describedAs;
    }

    public boolean matches(Object actual) {
        return (actual != null) &&
                (Primitives.isAssignableFromWrapper(actual.getClass(), clazz)
                        || clazz.isAssignableFrom(actual.getClass()));
    }

    public String toString() {
        return description;
    }

    public static class VarArgAware extends InstanceOf implements VarargMatcher {

        public VarArgAware(Class<?> clazz) {
            super(clazz);
        }

        public VarArgAware(Class<?> clazz, String describedAs) {
            super(clazz, describedAs);
        }
    }


}
