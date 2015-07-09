/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unchecked")
public class And implements ArgumentMatcher, Serializable {

    private final List<ArgumentMatcher> matchers;

    public And(List<ArgumentMatcher> matchers) {
        this.matchers = matchers;
    }

    public boolean matches(Object actual) {
        for (ArgumentMatcher matcher : matchers) {
            if (!matcher.matches(actual)) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("and(");
        for (Iterator<ArgumentMatcher> it = matchers.iterator(); it.hasNext();) {
            out.append(it.next().toString());
            if (it.hasNext()) {
                out.append(", ");
            }
        }
        out.append(")");
        return out.toString();
    }
}
