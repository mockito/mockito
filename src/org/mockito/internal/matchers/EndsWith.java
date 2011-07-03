/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import java.io.Serializable;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;


public class EndsWith extends ArgumentMatcher<String> implements Serializable {

    private static final long serialVersionUID = 8556443228350129421L;
    private final String suffix;

    public EndsWith(String suffix) {
        this.suffix = suffix;
    }

    public boolean matches(Object actual) {
        return actual != null && ((String) actual).endsWith(suffix);
    }

    public void describeTo(Description description) {
        description.appendText("endsWith(\"" + suffix + "\")");
    }
}