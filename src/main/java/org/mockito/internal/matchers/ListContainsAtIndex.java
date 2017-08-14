/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;

import java.util.List;

public class ListContainsAtIndex<T> implements ArgumentMatcher<List> {

    private final T object;
    private final int index;

    public ListContainsAtIndex(T object, int index) {
        this.object = object;
        this.index = index;
    }

    public boolean matches(List list) {
        return object == list.get(index);
    }

    public String toString() {
        return String.format("[list doesn't contain object: %s at index: %d]", object.toString(), index);
    }
}
