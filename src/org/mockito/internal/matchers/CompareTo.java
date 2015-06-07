/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;

@SuppressWarnings("rawtypes")
public abstract class CompareTo<T extends Comparable<T>> extends ArgumentMatcher<T> {
    private final Comparable<T> wanted;

    public CompareTo(final Comparable<T> value) {
        this.wanted = value;
    }

    @SuppressWarnings("unchecked")
    public boolean matches(final Object actual) {
        
        if(!(actual instanceof Comparable)) {
            return false;
        }
        
        return matchResult(((Comparable) actual).compareTo(wanted));
    }

    public void describeTo(final Description description) {
        description.appendText(getName() + "(" + wanted + ")");
    }
    
    protected abstract String getName();
    
    protected abstract boolean matchResult(final int result);
}
