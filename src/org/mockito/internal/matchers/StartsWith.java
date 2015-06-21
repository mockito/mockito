/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.mockito.MockitoMatcher;

import java.io.Serializable;

public class StartsWith extends MockitoMatcher<String> implements Serializable {

    private final String prefix;

    public StartsWith(String prefix) {
        this.prefix = prefix;
    }

    public boolean matches(Object actual) {
        return actual != null && ((String) actual).startsWith(prefix);
    }

    public String describe() {
        return "startsWith(\"" + prefix + "\")";
    }
}
