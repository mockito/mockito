/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.instance;

/**
 * @deprecated since 2.15.4 because this internal class was leaking from the public API.
 * For more information why deprecated, see {@link org.mockito.plugins.InstantiatorProvider2}.
 * Use {@link org.mockito.creation.instance.Instantiator} instead.
 * <p>
 * Provides instances of classes.
 */
@Deprecated
public interface Instantiator {

    /**
     * Creates instance of given class
     */
    <T> T newInstance(Class<T> cls) throws InstantiationException;

}
