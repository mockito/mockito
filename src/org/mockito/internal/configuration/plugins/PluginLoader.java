package org.mockito.internal.configuration.plugins;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.MockitoConfigurationException;
import org.mockito.internal.util.collections.Iterables;
import org.mockito.plugins.PluginSwitcher;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

class PluginLoader {

    private final PluginSwitcher pluginSwitcher;

    public PluginLoader(PluginSwitcher pluginSwitcher) {
        this.pluginSwitcher = pluginSwitcher;
    }

    /**
     * Scans the classpath for given pluginType. If not found, default class is used.
     */
    <T> T loadPlugin(Class<T> pluginType, String defaultPluginClassName) {
        for (T plugin : loadImplementations(pluginType)) {
            return plugin; // return the first one service loader finds (if any)
        }

        try {
            // Default implementation. Use our own ClassLoader instead of the context
            // ClassLoader, as the default implementation is assumed to be part of
            // Mockito and may not be available via the context ClassLoader.
            return pluginType.cast(Class.forName(defaultPluginClassName).newInstance());
        } catch (Exception e) {
            throw new MockitoException("Internal problem occurred, please report it. " +
                    "Mockito is unable to load the default implementation of class that is a part of Mockito distribution. " +
                    "Failed to load " + pluginType, e);
        }
    }

    /**
     * Equivalent to {@link java.util.ServiceLoader#load} but without requiring
     * Java 6 / Android 2.3 (Gingerbread).
     */
    <T> List<T> loadImplementations(Class<T> service) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
        }
        Enumeration<URL> resources;
        try {
            resources = loader.getResources("mockito-extensions/" + service.getName());
        } catch (IOException e) {
            throw new MockitoException("Failed to load " + service, e);
        }

        //TODO SF refactor
        List<T> result = new ArrayList<T>();
        try {
            String foundPluginClass = new PluginFinder(pluginSwitcher).findPluginClass(Iterables.toIterable(resources));
            if (foundPluginClass != null) {
                Class<?> pluginClass = loader.loadClass(foundPluginClass);
                Object plugin = pluginClass.newInstance();
                result.add(service.cast(plugin));
            }
            return result;
        } catch (Exception e) {
            throw new MockitoConfigurationException(
                    "Failed to load " + service + " implementation declared in " + resources, e);
        }
    }
}
