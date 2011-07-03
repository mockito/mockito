/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;

import java.io.Serializable;


public class Same extends ArgumentMatcher<Object> implements Serializable {

    private static final long serialVersionUID = -1226959355938572597L;
    private final Object wanted;

    public Same(Object wanted) {
        this.wanted = wanted;
    }

    public boolean matches(Object actual) {
        return wanted == actual;
    }

    public void describeTo(Description description) {
        description.appendText("same(");
        appendQuoting(description);
        description.appendText("" + wanted);
        appendQuoting(description);
        description.appendText(")");
    }

    private void appendQuoting(Description description) {
        if (wanted instanceof String) {
            description.appendText("\"");
        } else if (wanted instanceof Character) {
            description.appendText("'");
        }
    }
}
