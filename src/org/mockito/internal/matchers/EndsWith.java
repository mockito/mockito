/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import java.io.Serializable;

import org.mockito.MockitoMatcher;

public class EndsWith extends MockitoMatcher<String> implements Serializable {

    private final String suffix;

    public EndsWith(String suffix) {
        this.suffix = suffix;
    }

    public boolean matches(Object actual) {
        return actual != null && ((String) actual).endsWith(suffix);
    }

    public String describe() {
        return "endsWith(\"" + suffix + "\")";
    }
}