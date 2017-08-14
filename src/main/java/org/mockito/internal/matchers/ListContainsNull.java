/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.matchers;

import org.mockito.ArgumentMatcher;

import java.util.List;

public class ListContainsNull implements ArgumentMatcher<List> {

    public boolean matches(List list) {
        return list.contains(null);
    }

    @Override
    public String toString() {
        return "[list does not contain null]";
    }
}
