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