/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;
import org.mockito.internal.matchers.text.ValuePrinter;

import java.io.Serializable;

public class Same implements ArgumentMatcher<Object>, Serializable {

    private final Object wanted;

    public Same(Object wanted) {
        this.wanted = wanted;
    }

    public boolean matches(Object actual) {
        return wanted == actual;
    }

    public String toString() {
        return "same(" + ValuePrinter.print(wanted) + ")";
    }
}
