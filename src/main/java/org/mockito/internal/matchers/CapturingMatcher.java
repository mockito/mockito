
/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.io.Serializable;
import java.util.List;

import org.mockito.ArgumentMatcher;

public class CapturingMatcher<T>
        implements ArgumentMatcher<T>, CapturesArguments, VarargMatcher, Serializable {


    private final List<T> arguments;
    private final ArgumentMatcher<T> matcher;

    public CapturingMatcher(ArgumentMatcher<T> matcher, List<T> arguments) {
        this.matcher = matcher;
        this.arguments = arguments;
    }

    public String toString() {
        return "<Capturing argument>";
    }

    public void captureFrom(Object argument) {
        if (matcher.matches((T)argument)) {
            this.arguments.add((T) argument);
        }
    }

    @Override
    public boolean matches(Object argument) {
        // vararg arguments will be filtered out when adding to arguments
        if (argument != null && argument.getClass().isArray()) {
            return true;
        }
        return matcher.matches((T)argument);
    }
}