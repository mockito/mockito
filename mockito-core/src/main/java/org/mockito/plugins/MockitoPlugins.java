/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.plugins;

import org.mockito.Mockito;
import org.mockito.MockitoFramework;
import org.mockito.NotExtensible;

/**
 * Instance of this interface is available via {@link MockitoFramework#getPlugins()}.
 * This object enables framework integrators to get access to default Mockito plugins.
 * <p>
 * Example use case: one needs to implement custom {@link MockMaker}
 * and delegate some behavior to the default Mockito implementation.
 * The interface was introduced to help framework integrations.
 *
 * @since 2.10.0
 */
@NotExtensible
public interface MockitoPlugins {

    /**
     * Returns the default plugin implementation used by Mockito.
     * Mockito plugins are stateless so it is recommended to keep hold of the returned plugin implementation
     * rather than calling this method multiple times.
     * Each time this method is called, new instance of the plugin is created.
     *
     * @param pluginType type of the plugin, for example {@link MockMaker}.
     * @return the plugin instance
     * @since 2.10.0
     */
    <T> T getDefaultPlugin(Class<T> pluginType);

    /**
     * Returns inline mock maker, an optional mock maker that is bundled with Mockito distribution.
     * This method is needed because {@link #getDefaultPlugin(Class)} does not provide an instance of inline mock maker.
     * Creates new instance each time is called so it is recommended to keep hold of the resulting object for future invocations.
     * For more information about inline mock maker see the javadoc for main {@link Mockito} class.
     *
     * @return instance of inline mock maker
     * @since 2.10.0
     * @deprecated Please use {@link #getMockMaker(String)} with {@link org.mockito.MockMakers#INLINE} instead.
     */
    @Deprecated(since = "5.6.0", forRemoval = true)
    MockMaker getInlineMockMaker();

    /**
     * Returns {@link MockMaker} instance used by Mockito with the passed name {@code mockMaker}.
     *
     * <p>This will return the instance used by Mockito itself, not a new instance of it.
     *
     * <p>This method can be used to increase the interop of mocks created by Mockito and other
     * libraries using Mockito mock maker API.
     *
     * @param mockMaker the name of the mock maker or {@code null} to retrieve the default mock maker
     * @return instance of the mock maker
     * @throws IllegalStateException if a mock maker with the name is not found
     * @since 5.6.0
     */
    default MockMaker getMockMaker(String mockMaker) {
        throw new UnsupportedOperationException("This method is not implemented.");
    }
}
