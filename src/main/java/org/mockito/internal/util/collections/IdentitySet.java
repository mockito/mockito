/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.collections;

import java.util.ArrayList;

public class IdentitySet {

    private final ArrayList<Object> list = new ArrayList<>();

    public boolean contains(Object o) {
        for (Object existing : list) {
            if (existing == o) {
                return true;
            }
        }
        return false;
    }

    public void add(Object o) {
        list.add(o);
    }
}
