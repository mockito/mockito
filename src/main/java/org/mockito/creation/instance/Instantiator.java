/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.creation.instance;

/**
 * Provides instances of classes.
 * See more information about Mockito plugin {@link org.mockito.plugins.InstantiatorProvider2}
 *
 * @since 2.16.0
 */
public interface Instantiator {

    /**
     * Creates instance of given class
     *
     * @since 2.16.0
     */
    <T> T newInstance(Class<T> cls) throws InstantiationException;

}
