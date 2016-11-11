/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.instance;

/**
 * Provides instances of classes.
 */
public interface Instantiator {

    /**
     * Creates instance of given class
     */
    <T> T newInstance(Class<T> cls) throws InstantiationException;

}
