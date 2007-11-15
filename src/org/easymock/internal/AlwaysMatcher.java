/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

import org.easymock.AbstractMatcher;

public class AlwaysMatcher extends AbstractMatcher {
    public boolean matches(Object[] expected, Object[] actual) {
        return true;
    }

    protected String argumentToString(Object argument) {
        return "<any>";
    }
}