/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;

import java.io.Serializable;

public class EqualsWithDelta implements ArgumentMatcher<Number>, Serializable {

    private final Number wanted;
    private final Number delta;

    public EqualsWithDelta(Number value, Number delta) {
        this.wanted = value;
        this.delta = delta;
    }

    public boolean matches(Number actual) {
        if (wanted == null ^ actual == null) {
            return false;
        }

        if (wanted == actual) {
            return true;
        }

        return wanted.doubleValue() - delta.doubleValue() <= actual.doubleValue()
                && actual.doubleValue() <= wanted.doubleValue()
                        + delta.doubleValue();
    }

    public String toString() {
        return "eq(" + wanted + ", " + delta + ")";
    }
}
