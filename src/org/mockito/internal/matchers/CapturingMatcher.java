/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;
import org.mockito.exceptions.Reporter;

@SuppressWarnings({"unchecked", "rawtypes"})
public class CapturingMatcher<T> extends ArgumentMatcher<T> implements CapturesArguments, VarargMatcher, Serializable {
    
    private static final long serialVersionUID = 4274067078639307295L;
    private final LinkedList<Object> arguments = new LinkedList<Object>();

    /* (non-Javadoc)
     * @see org.mockito.ArgumentMatcher#matches(java.lang.Object)
     */
    public boolean matches(final Object argument) {
        return true;
    }    

    /* (non-Javadoc)
     * @see org.mockito.ArgumentMatcher#describeTo(org.hamcrest.Description)
     */
    public void describeTo(final Description description) {
        description.appendText("<Capturing argument>");
    }

    public T getLastValue() {
        if (arguments.isEmpty()) {
            new Reporter().noArgumentValueWasCaptured();
            return null;
        } else {
            return (T) arguments.getLast();
        }
    }

    public List<T> getAllValues() {
        return (List) arguments;
    }

    public void captureFrom(final Object argument) {
        this.arguments.add(argument);
    }
}