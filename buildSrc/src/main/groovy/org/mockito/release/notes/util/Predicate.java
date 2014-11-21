package org.mockito.release.notes.util;

/**
 * Generic predicate
 */
public interface Predicate<T> {

    /**
     * returns true if the predicate is satisfied for given object
     */
    boolean isTrue(T object);
}
