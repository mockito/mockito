/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal.matchers;

import java.util.Iterator;
import java.util.List;

import org.easymock.IArgumentMatcher;

public class Or implements IArgumentMatcher {

    private final List<IArgumentMatcher> matchers;

    public Or(List<IArgumentMatcher> matchers) {
        this.matchers = matchers;
    }

    public boolean matches(Object actual) {
        for (IArgumentMatcher matcher : matchers) {
            if (matcher.matches(actual)) {
                return true;
            }
        }
        return false;
    }

    public void appendTo(StringBuffer buffer) {
        buffer.append("or(");
        for (Iterator<IArgumentMatcher> it = matchers.iterator(); it.hasNext();) {
            it.next().appendTo(buffer);
            if (it.hasNext()) {
                buffer.append(", ");
            }
        }
        buffer.append(")");
    }
}
