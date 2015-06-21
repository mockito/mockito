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
public class Or extends MockitoMatcher implements Serializable {

    private final List<MockitoMatcher> matchers;

    public Or(List<MockitoMatcher> matchers) {
        this.matchers = matchers;
    }

    public boolean matches(Object actual) {
        for (MockitoMatcher matcher : matchers) {
            if (matcher.matches(actual)) {
                return true;
            }
        }
        return false;
    }

    public String describe() {
        //TODO SF here and in other places we should reuse ValuePrinter
        StringBuilder sb = new StringBuilder("or(");
        for (Iterator<MockitoMatcher> it = matchers.iterator(); it.hasNext();) {
            sb.append(it.next().describe());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.append(")").toString();
    }
}
