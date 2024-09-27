/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.plugins;

/**
 * A mock resolver offers an opportunity to resolve a mock from any instance that is
 * provided to the {@link org.mockito.Mockito}-DSL. This mechanism can be used by
 * frameworks that provide mocks that are implemented by Mockito but which are wrapped
 * by other instances to enhance the proxy further.
 */
public interface MockResolver {

    /**
     * Returns the provided instance or the unwrapped mock that the provided
     * instance represents. This method must not return {@code null}.
     * @param instance The instance passed to the {@link org.mockito.Mockito}-DSL.
     * @return The provided instance or the unwrapped mock.
     */
    Object resolve(Object instance);
}
