/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal.matchers;

import org.easymock.IArgumentMatcher;

public class InstanceOf implements IArgumentMatcher {

    private final Class<?> clazz;

    public InstanceOf(Class clazz) {
        this.clazz = clazz;
    }

    public boolean matches(Object actual) {
        return (actual != null) && clazz.isAssignableFrom(actual.getClass());
    }

    public void appendTo(StringBuffer buffer) {
        buffer.append("isA(" + clazz.getName() + ")");
    }
}
