/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;


public class Equals extends ArgumentMatcher<Object> {

    private final Object wanted;

    public Equals(Object wanted) {
        this.wanted = wanted;
    }

    public boolean matches(Object actual) {
        if (this.wanted == null) {
            return actual == null;
        }
        return wanted.equals(actual);
    }

    public void describeTo(Description description) {
        appendQuoting(description);
        if (wanted == null) {
            description.appendText("null");
        } else {
            description.appendText(wanted.toString());
        }
        appendQuoting(description);
    }

    private void appendQuoting(Description description) {
        if (wanted instanceof String) {
            description.appendText("\"");
        } else if (wanted instanceof Character) {
            description.appendText("'");
        }
    }

    protected final Object getWanted() {
        return wanted;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !this.getClass().equals(o.getClass())) {
            return false;
        }
        Equals other = (Equals) o;
        return this.wanted == null && other.wanted == null
                || this.wanted != null
                && this.wanted.equals(other.wanted);
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("hashCode() is not supported");
    }
}