/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.plugins;

import org.mockito.internal.util.collections.Iterables;
import org.mockito.plugins.PluginSwitch;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

class PluginLoader {

    private final PluginSwitch pluginSwitch;

    private final Map<String, String> alias;

    public PluginLoader(PluginSwitch pluginSwitch) {
        this.pluginSwitch = pluginSwitch;
        this.alias = new HashMap<String, String>();
    }

    /**
     * Adds an alias for a class name to this plugin loader. Instead of the fully qualified type name,
     * the alias can be used as a convenience name for a known plugin.
     */
    PluginLoader withAlias(String name, String type) {
        alias.put(name, type);
        return this;
    }

    /**
     * Scans the classpath for given pluginType. If not found, default class is used.
     */
    <T> T loadPlugin(Class<T> pluginType, String defaultPluginClassName) {
        T plugin = loadImpl(pluginType);
        if (plugin != null) {
            return plugin;
        }

        try {
            // Default implementation. Use our own ClassLoader instead of the context
            // ClassLoader, as the default implementation is assumed to be part of
            // Mockito and may not be available via the context ClassLoader.
            return pluginType.cast(Class.forName(defaultPluginClassName).newInstance());
        } catch (Exception e) {
            throw new IllegalStateException("Internal problem occurred, please report it. " +
                    "Mockito is unable to load the default implementation of class that is a part of Mockito distribution. " +
                    "Failed to load " + pluginType, e);
        }
    }

    /**
     * Equivalent to {@link java.util.ServiceLoader#load} but without requiring
     * Java 6 / Android 2.3 (Gingerbread).
     */
    private <T> T loadImpl(Class<T> service) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
        }
        Enumeration<URL> resources;
        try {
            resources = loader.getResources("mockito-extensions/" + service.getName());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load " + service, e);
        }

        try {
            String foundPluginClass = new PluginFinder(pluginSwitch).findPluginClass(Iterables.toIterable(resources));
            if (foundPluginClass != null) {
                String aliasType = alias.get(foundPluginClass);
                if (aliasType != null) {
                    foundPluginClass = aliasType;
                }
                Class<?> pluginClass = loader.loadClass(foundPluginClass);
                Object plugin = pluginClass.newInstance();
                return service.cast(plugin);
            }
            return null;
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to load " + service + " implementation declared in " + resources, e);
        }
    }
}
