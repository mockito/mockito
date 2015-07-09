package org.mockito.internal.matchers.text;

import java.lang.reflect.Array;
import java.util.Iterator;

/**
 * Inspired on hamcrest, internal package class,
 * TODO add specific unit tests instead of relying on higher level unit tests
 */
class ArrayIterator implements Iterator<Object> {

    private final Object array;
    private int currentIndex = 0;

    public ArrayIterator(Object array) {
        if (array == null) {
            //TODO extract a small utility for null-checking
            throw new IllegalArgumentException("Expected array instance but got null");
        }
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException("Expected array but got object of type: "
                    + array.getClass() + ", the object: " + array.toString());
        }
        this.array = array;
    }

    public boolean hasNext() {
        return currentIndex < Array.getLength(array);
    }

    public Object next() {
        return Array.get(array, currentIndex++);
    }

    public void remove() {
        throw new UnsupportedOperationException("cannot remove items from an array");
    }
}
