/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

import org.easymock.AbstractMatcher;
import org.easymock.internal.matchers.ArrayEquals;

public class ArrayMatcher extends AbstractMatcher {
    public String argumentToString(Object argument) {
        StringBuffer result = new StringBuffer();
        new ArrayEquals(argument).appendTo(result);
        return result.toString();
    }

    public boolean argumentMatches(Object expected, Object actual) {
        return new ArrayEquals(expected).matches(actual);
    }
}