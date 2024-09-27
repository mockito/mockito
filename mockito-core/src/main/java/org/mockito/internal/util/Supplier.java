/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
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
