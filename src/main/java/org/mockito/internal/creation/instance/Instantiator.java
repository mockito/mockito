/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.instance;

/**
 * Provides instances of classes.
 *
 * <p>This interface is considered public even though it lives in private package.
 * In the next major version of Mockito, this class will be moved to public space.
 * </p>
 *
 * @since 2.14.0
 */
public interface Instantiator {

    /**
     * Creates instance of given class
     */
    <T> T newInstance(Class<T> cls) throws InstantiationException;

}
