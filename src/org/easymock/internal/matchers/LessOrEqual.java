/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal.matchers;

public class LessOrEqual<T extends Comparable<T>> extends CompareTo<T> {

    public LessOrEqual(Comparable<T> value) {
        super(value);
    }

    @Override
    protected String getName() {
        return "leq";
    }

    @Override
    protected boolean matchResult(int result) {
        return result <= 0;
    }
}
