/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;


public class InstanceOf implements IArgumentMatcher {

    private final Class<?> clazz;

    public InstanceOf(Class<?> clazz) {
        this.clazz = clazz;
    }

    public boolean matches(Object actual) {
        return (actual != null) && clazz.isAssignableFrom(actual.getClass());
    }

    public void appendTo(StringBuilder buffer) {
        buffer.append("isA(" + clazz.getName() + ")");
    }
}
