/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.internal.matchers.ArgumentMatcher;

/**
 * TODO document
 * @param <T>
 */
public abstract class CustomMatcher<T> implements ArgumentMatcher<T> {
    /* 
     * @see org.mockito.internal.matchers.ArgumentMatcher#appendTo(java.lang.StringBuilder)
     */
    public void appendTo(StringBuilder builder) {
        builder.append("<custom argument matcher>");
    }

    public abstract boolean matches(T argument);
}