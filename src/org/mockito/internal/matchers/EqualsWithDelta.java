/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;


public class EqualsWithDelta implements ArgumentMatcher<Number>{
    private final Number wanted;

    private final Number delta;

    public EqualsWithDelta(Number value, Number delta) {
        this.wanted = value;
        this.delta = delta;
    }

    public boolean matches(Number actual) {
        return wanted.doubleValue() - delta.doubleValue() <= actual.doubleValue()
                && actual.doubleValue() <= wanted.doubleValue()
                        + delta.doubleValue();
    }

    public void appendTo(StringBuilder buffer) {
        buffer.append("eq(" + wanted + ", " + delta + ")");
    }
}
