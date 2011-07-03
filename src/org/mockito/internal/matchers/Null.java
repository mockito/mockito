/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import java.io.Serializable;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;


public class Null extends ArgumentMatcher<Object> implements Serializable {

    private static final long serialVersionUID = 2823082637424390314L;
    public static final Null NULL = new Null();

    private Null() {
    }

    public boolean matches(Object actual) {
        return actual == null;
    }

    public void describeTo(Description description) {
        description.appendText("isNull()");
    }
}
