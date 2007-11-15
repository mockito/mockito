/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal.matchers;

import org.easymock.IArgumentMatcher;

public class EqualsWithDelta implements IArgumentMatcher {
    private final Number expected;

    private final Number delta;

    public EqualsWithDelta(Number value, Number delta) {
        this.expected = value;
        this.delta = delta;
    }

    public boolean matches(Object actual) {
        Number actualNumber = (Number) actual;
        return expected.doubleValue() - delta.doubleValue() <= actualNumber
                .doubleValue()
                && actualNumber.doubleValue() <= expected.doubleValue()
                        + delta.doubleValue();
    }

    public void appendTo(StringBuffer buffer) {
        buffer.append("eq(" + expected + ", " + delta + ")");
    }
}
