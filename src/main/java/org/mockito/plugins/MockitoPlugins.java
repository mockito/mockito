package org.mockito.plugins;

import org.mockito.MockitoFramework;

/**
 * Instance of this interface is available via {@link MockitoFramework#getPlugins()}.
 *
 * TODO document example use scenario
 */
public interface MockitoPlugins {

    /**
     * Returns the default plugin implementation used by Mockito.
     * Mockito plugins are stateless so it is recommended to keep hold of the returned plugin implementation
     * rather than calling this method multiple times.
     *
     * @param pluginType
     * @return the plugin instance
     */
    <T> T getDefaultPlugin(Class<T> pluginType);
}
