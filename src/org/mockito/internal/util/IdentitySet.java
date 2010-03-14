package org.mockito.internal.util;

import java.util.LinkedList;

@SuppressWarnings("unchecked")
public class IdentitySet {

    LinkedList list = new LinkedList();
    
    public boolean contains(Object o) {
        for(Object existing:list) {
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