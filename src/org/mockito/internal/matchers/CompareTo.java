/*
 * Copyright (c) 2007 Mockito contributors 
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;


public abstract class CompareTo<T extends Comparable<T>> implements IArgumentMatcher {
    private final Comparable<T> expected;

    public CompareTo(Comparable<T> value) {
        this.expected = value;
    }

    @SuppressWarnings("unchecked")
    public boolean matches(Object actual) {
        
        if(!(actual instanceof Comparable)) {
            return false;
        }
        
        return matchResult(((Comparable) actual).compareTo(expected));
    }

    public void appendTo(StringBuffer buffer) {
        buffer.append(getName() + "(" + expected + ")");
    }
    
    protected abstract String getName();
    
    protected abstract boolean matchResult(int result);
}
