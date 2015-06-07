/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import java.io.Serializable;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.mockito.ArgumentMatcher;

@SuppressWarnings("rawtypes")
public class Not extends ArgumentMatcher implements Serializable {

    private static final long serialVersionUID = 4627373642333593264L;
    private final Matcher first;

    public Not(final Matcher first) {
        this.first = first;
    }

    public boolean matches(final Object actual) {
        return !first.matches(actual);
    }

    public void describeTo(final Description description) {
        description.appendText("not(");
        first.describeTo(description);
        description.appendText(")");
    }
}