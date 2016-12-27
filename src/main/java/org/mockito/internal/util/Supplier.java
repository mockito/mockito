package org.mockito.internal.util;

/**
 * Abstract provider that can supply (provide/create) objects of given type.
 *
 * Delete when we move to Java 8 and use built-in type.
 *
 * @param <T> type of object to provide/create.
 */
public interface Supplier<T> {

    /**
     * Provides an instance.
     */
    T get();
}
