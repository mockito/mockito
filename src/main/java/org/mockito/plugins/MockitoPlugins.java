/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.plugins;

import org.mockito.Mockito;
import org.mockito.MockitoFramework;

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
     */
    MockMaker getInlineMockMaker();
}
