/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;

import java.io.Serializable;

public abstract class CompareTo<T extends Comparable<T>> implements ArgumentMatcher<T>, Serializable {
    private final T wanted;

    public CompareTo(T value) {
        this.wanted = value;
    }

    @Override
    public final boolean matches(T actual) {
        if (actual == null) {
            return false;
        }
        if (!actual.getClass().isInstance(wanted)){ 
            return false;
        }
       
        int result = actual.compareTo(wanted);
        return matchResult(result);
    }

    @Override
    public final String toString() {
        return getName() + "(" + wanted + ")";
    }

    protected abstract String getName();

    protected abstract boolean matchResult(int result);
}
