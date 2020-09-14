/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.plugins;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;

import org.mockito.plugins.PluginSwitch;

class PluginLoader {

    private final DefaultMockitoPlugins plugins;
    private final PluginInitializer initializer;

    PluginLoader(DefaultMockitoPlugins plugins, PluginInitializer initializer) {
        this.plugins = plugins;
        this.initializer = initializer;
    }

    PluginLoader(PluginSwitch pluginSwitch) {
        this(
                new DefaultMockitoPlugins(),
                new PluginInitializer(pluginSwitch, null, new DefaultMockitoPlugins()));
    }

    /**
     * @deprecated Let's avoid adding more aliases. It complicates the API.
     * Instead of an alias, we can use fully qualified class name of the alternative implementation.
     * <p>
     * Adds an alias for a class name to this plugin loader. Instead of the fully qualified type name,
     * the alias can be used as a convenience name for a known plugin.
     */
    @Deprecated
    PluginLoader(PluginSwitch pluginSwitch, String alias) {
        this(
                new DefaultMockitoPlugins(),
                new PluginInitializer(pluginSwitch, alias, new DefaultMockitoPlugins()));
    }

    /**
     * Scans the classpath for given pluginType. If not found, default class is used.
     */
    @SuppressWarnings("unchecked")
    <T> T loadPlugin(final Class<T> pluginType) {
        return (T) loadPlugin(pluginType, null);
    }

    /**
     * Scans the classpath for given {@code preferredPluginType}. If not found scan for {@code
     * alternatePluginType}. If neither a preferred or alternate plugin is found, default to default
     * class of {@code preferredPluginType}.
     *
     * @return An object of either {@code preferredPluginType} or {@code alternatePluginType}
     */
    @SuppressWarnings("unchecked")
    <PreferredType, AlternateType> Object loadPlugin(
            final Class<PreferredType> preferredPluginType,
            final Class<AlternateType> alternatePluginType) {
        try {
            PreferredType preferredPlugin = initializer.loadImpl(preferredPluginType);
            if (preferredPlugin != null) {
                return preferredPlugin;
            } else if (alternatePluginType != null) {
                AlternateType alternatePlugin = initializer.loadImpl(alternatePluginType);
                if (alternatePlugin != null) {
                    return alternatePlugin;
                }
            }

            return plugins.getDefaultPlugin(preferredPluginType);
        } catch (final Throwable t) {
            return Proxy.newProxyInstance(
                    preferredPluginType.getClassLoader(),
                    new Class<?>[] {preferredPluginType},
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args)
                                throws Throwable {
                            throw new IllegalStateException(
                                    "Could not initialize plugin: "
                                            + preferredPluginType
                                            + " (alternate: "
                                            + alternatePluginType
                                            + ")",
                                    t);
                        }
                    });
        }
    }

    /**
     * Scans the classpath for given {@code pluginType} and returns a list of its instances.
     *
     * @return An list of {@code pluginType} or an empty list if none was found.
     */
    @SuppressWarnings("unchecked")
    <T> List<T> loadPlugins(final Class<T> pluginType) {
        try {
            return initializer.loadImpls(pluginType);
        } catch (final Throwable t) {
            return Collections.singletonList(
                    (T)
                            Proxy.newProxyInstance(
                                    pluginType.getClassLoader(),
                                    new Class<?>[] {pluginType},
                                    new InvocationHandler() {
                                        @Override
                                        public Object invoke(
                                                Object proxy, Method method, Object[] args)
                                                throws Throwable {
                                            throw new IllegalStateException(
                                                    "Could not initialize plugin: " + pluginType,
                                                    t);
                                        }
                                    }));
        }
    }
}
