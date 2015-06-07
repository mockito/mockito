/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.collections;

import java.util.LinkedList;

@SuppressWarnings({"unchecked", "rawtypes"})
public class IdentitySet {

    LinkedList list = new LinkedList();
    
    public boolean contains(final Object o) {
        for(final Object existing:list) {
            if (existing == o) {
                return true;
            }
        }
        return false;
    }

    public void add(final Object o) {
        list.add(o);        
    }
}