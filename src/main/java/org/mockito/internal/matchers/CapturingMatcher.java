/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;

import static org.mockito.internal.exceptions.Reporter.noArgumentValueWasCaptured;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unchecked")
public class CapturingMatcher<T> implements ArgumentMatcher<T>, CapturesArguments, VarargMatcher, Serializable {
    
    private final LinkedList<Object> arguments = new LinkedList<Object>();

    public boolean matches(Object argument) {
        return true;
    }    

    public String toString() {
        return "<Capturing argument>";
    }

    public T getLastValue() {
        if (arguments.isEmpty()) {
            throw noArgumentValueWasCaptured();
        }
        
        return (T) arguments.getLast();
        
    }

    public List<T> getAllValues() {
        return (List) arguments;
    }

    public void captureFrom(Object argument) {
        this.arguments.add(argument);
    }
}
