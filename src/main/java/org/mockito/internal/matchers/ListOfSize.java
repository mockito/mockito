/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;

import java.util.List;

public class ListOfSize implements ArgumentMatcher<List> {

    private final int size;
    private int actualSize;

    public ListOfSize(int size) {
        this.size = size;
    }

    public boolean matches(List list) {
        actualSize = list.size();
        return actualSize == size;
    }

    public String toString() {
        //printed in verification errors
        return String.format("[list has wrong size: expected %d, actual %d]", size, actualSize);
    }
}
