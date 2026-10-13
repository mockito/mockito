/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.io.Serializable;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.text.ValuePrinter;

public class Same implements ArgumentMatcher<Object>, Serializable {

    private final Object wanted;

    public Same(Object wanted) {
        this.wanted = wanted;
    }

    @Override
    public boolean matches(Object actual) {
        return wanted == actual;
    }

    @Override
    public Class<?> type() {
        return wanted != null ? wanted.getClass() : ArgumentMatcher.super.type();
    }

    @Override
    public String toString() {
        return "same(" + ValuePrinter.print(wanted) + ")";
    }
}
