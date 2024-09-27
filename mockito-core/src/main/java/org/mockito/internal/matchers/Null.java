/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.io.Serializable;
import java.util.Objects;

import org.mockito.ArgumentMatcher;

public class Null<T> implements ArgumentMatcher<T>, Serializable {

    public static final Null<Object> NULL = new Null<>(Object.class);
    private final Class<T> type;

    public Null(Class<T> type) {
        this.type = Objects.requireNonNull(type);
    }

    @Override
    public boolean matches(Object actual) {
        return actual == null;
    }

    @Override
    public Class<T> type() {
        return type;
    }

    @Override
    public String toString() {
        return "isNull()";
    }
}
