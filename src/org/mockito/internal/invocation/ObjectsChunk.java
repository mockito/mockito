package org.mockito.internal.invocation;

import java.util.LinkedList;
import java.util.List;

public class ObjectsChunk<T> {
    
    LinkedList<T> objects = new LinkedList<T>(); 

    public ObjectsChunk() {}
    
    public ObjectsChunk(T object) {
        objects.add(object);
    }

    public void addObject(T object) {
        objects.add(object);
    }
    
    public T getObject() {
        return objects.getFirst();
    }
    
    public List<T> getObjects() {
        return objects;
    }
    
    public int getSize() {
        return objects.size();
    }

    public boolean isEmpty() {
        return getSize() == 0;
    }
    
    @Override
    public String toString() {
        return "Chunk size: " + getSize() + ", objects: " + objects;
    }
}