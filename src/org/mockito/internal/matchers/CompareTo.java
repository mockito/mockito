/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;


public abstract class CompareTo<T extends Comparable<T>> extends ArgumentMatcher<T> {
    private final Comparable<T> wanted;

    public CompareTo(Comparable<T> value) {
        this.wanted = value;
    }

    @SuppressWarnings("unchecked")
    public boolean matches(Object actual) {
        
        if(!(actual instanceof Comparable)) {
            return false;
        }
        
        return matchResult(((Comparable) actual).compareTo(wanted));
    }

    public void describeTo(Description description) {
        description.appendText(getName() + "(" + wanted + ")");
    }
    
    protected abstract String getName();
    
    protected abstract boolean matchResult(int result);
}
