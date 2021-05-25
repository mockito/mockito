/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.plugins;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.mockito.internal.util.collections.Iterables;
import org.mockito.plugins.PluginSwitch;

class PluginInitializer {

    private final PluginSwitch pluginSwitch;
    private final String alias;
    private final DefaultMockitoPlugins plugins;

    PluginInitializer(PluginSwitch pluginSwitch, String alias, DefaultMockitoPlugins plugins) {
        this.pluginSwitch = pluginSwitch;
        this.alias = alias;
        this.plugins = plugins;
    }

    /**
     * Equivalent to {@link java.util.ServiceLoader#load} but without requiring
     * Java 6 / Android 2.3 (Gingerbread).
     */
    public <T> T loadImpl(Class<T> service) {
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
            String classOrAlias =
                    new PluginFinder(pluginSwitch).findPluginClass(Iterables.toIterable(resources));
            if (classOrAlias != null) {
                if (classOrAlias.equals(alias)) {
                    classOrAlias = plugins.getDefaultPluginClass(alias);
                }
                Class<?> pluginClass = loader.loadClass(classOrAlias);
                Object plugin = pluginClass.getDeclaredConstructor().newInstance();
                return service.cast(plugin);
            }
            return null;
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to load " + service + " implementation declared in " + resources, e);
        }
    }

    public <T> List<T> loadImpls(Class<T> service) {
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
            List<String> classesOrAliases =
                    new PluginFinder(pluginSwitch)
                            .findPluginClasses(Iterables.toIterable(resources));
            List<T> impls = new ArrayList<>();
            for (String classOrAlias : classesOrAliases) {
                if (classOrAlias.equals(alias)) {
                    classOrAlias = plugins.getDefaultPluginClass(alias);
                }
                Class<?> pluginClass = loader.loadClass(classOrAlias);
                Object plugin = pluginClass.getDeclaredConstructor().newInstance();
                impls.add(service.cast(plugin));
            }
            return impls;
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to load " + service + " implementation declared in " + resources, e);
        }
    }
}
