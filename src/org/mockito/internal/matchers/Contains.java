/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import java.io.Serializable;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;


public class Contains extends ArgumentMatcher<String> implements Serializable {

    private static final long serialVersionUID = -1909837398271763801L;
    private final String substring;

    public Contains(String substring) {
        this.substring = substring;
    }

    public boolean matches(Object actual) {
        return actual != null && ((String) actual).contains(substring);
    }

    public void describeTo(Description description) {
        description.appendText("contains(\"" + substring + "\")");
    }
}
