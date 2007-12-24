package org.mockito.internal.invocation;

import java.util.LinkedList;
import java.util.List;

public class ObjectsChunk<T> {
    
    LinkedList<T> objects = new LinkedList<T>(); 

    public ObjectsChunk(T object) {
        objects.add(object);
    }

    public int getSize() {
        return objects.size();
    }

    public List<T> getObjects() {
        return objects;
    }

    public T getObject() {
        return objects.getFirst();
    }

    public void addObject(T object) {
        objects.add(object);
    }
}
