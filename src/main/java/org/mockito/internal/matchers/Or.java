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
public class Or implements ArgumentMatcher, Serializable {

    private final List<ArgumentMatcher> matchers;

    public Or(List<ArgumentMatcher> matchers) {
        this.matchers = matchers;
    }

    public boolean matches(Object actual) {
        for (ArgumentMatcher matcher : matchers) {
            if (matcher.matches(actual)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        //TODO SF here and in other places we should reuse ValuePrinter
        StringBuilder sb = new StringBuilder("or(");
        for (Iterator<ArgumentMatcher> it = matchers.iterator(); it.hasNext();) {
            sb.append(it.next().toString());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.append(")").toString();
    }
}
