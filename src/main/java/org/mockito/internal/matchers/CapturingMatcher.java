/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;

import static org.mockito.internal.exceptions.Reporter.noArgumentValueWasCaptured;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
public class CapturingMatcher<T> implements ArgumentMatcher<T>, CapturesArguments, VarargMatcher, Serializable {

    private final List<Object> arguments = Collections.synchronizedList(new ArrayList<Object>());

    public boolean matches(Object argument) {
        return true;
    }

    public String toString() {
        return "<Capturing argument>";
    }

    public T getLastValue() {
        synchronized (arguments) {
            if (arguments.isEmpty()) {
                throw noArgumentValueWasCaptured();
            }

            return (T) arguments.get(arguments.size() - 1);
        }
    }

    public List<T> getAllValues() {
        return Arrays.asList((T[]) arguments.toArray());
    }

    public void captureFrom(Object argument) {
        this.arguments.add(argument);
    }
}
