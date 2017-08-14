/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;

import java.util.List;

public class ListDoesNotContain<T> implements ArgumentMatcher<List> {

    private final T[] objects;

    public ListDoesNotContain(T... objects) {
        this.objects = objects;
    }

    public boolean matches(List list) {
        for (T object : objects) {
            if (list.contains(object)) return false;
        }
        return true;
    }

    public String toString() {
        //printed in verification errors
        if (objects.length == 1) {
            return String.format("[list does contain object: %s]", objects[0].toString());
        } else {
            return "[list does contain one or more objects]";
        }
    }
}
