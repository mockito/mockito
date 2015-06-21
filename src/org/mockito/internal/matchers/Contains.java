/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.mockito.MockitoMatcher;

import java.io.Serializable;


public class Contains extends MockitoMatcher<String> implements Serializable {

    private final String substring;

    public Contains(String substring) {
        this.substring = substring;
    }

    public boolean matches(Object actual) {
        return actual != null && ((String) actual).contains(substring);
    }

    public String describe() {
        return "contains(\"" + substring + "\")";
    }
}
