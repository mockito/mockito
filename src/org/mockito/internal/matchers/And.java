/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.mockito.MockitoMatcher;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unchecked")
public class And extends MockitoMatcher implements Serializable {

    private final List<MockitoMatcher> matchers;

    public And(List<MockitoMatcher> matchers) {
        this.matchers = matchers;
    }

    public boolean matches(Object actual) {
        for (MockitoMatcher matcher : matchers) {
            if (!matcher.matches(actual)) {
                return false;
            }
        }
        return true;
    }

    public String describe() {
        StringBuilder out = new StringBuilder();
        out.append("and(");
        for (Iterator<MockitoMatcher> it = matchers.iterator(); it.hasNext();) {
            out.append(it.next().describe());
            if (it.hasNext()) {
                out.append(", ");
            }
        }
        out.append(")");
        return out.toString();
    }
}
