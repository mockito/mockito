/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;

import java.io.Serializable;


public class Contains implements ArgumentMatcher<String>, Serializable {

    private final String substring;

    public Contains(String substring) {
        this.substring = substring;
    }

    public boolean matches(String actual) {
        return actual != null && actual.contains(substring);
    }

    public String toString() {
        return "contains(\"" + substring + "\")";
    }
}
