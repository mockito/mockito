/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;


public class ObjectBox {

    private Object object;

    public void put(Object object) {
        this.object = object;
    }
    
    public Object getObject() {
        return object;
    }
}