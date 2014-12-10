package org.mockito.internal.configuration.plugins;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.MockitoConfigurationException;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

class PluginLoader {

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

        List<T> result = new ArrayList<T>();
        for (URL resource : Collections.list(resources)) {
            try {
                InputStream in = resource.openStream();
                InputStreamReader reader = new InputStreamReader(in, "UTF-8");
                String className = new PluginFileReader().readPluginClass(reader);
                if (className == null) {
                    //For backwards compatibility
                    //If the resource does not have plugin class name we're ignoring it
                    continue;
                }
                Class<?> pluginClass = loader.loadClass(className);
                Object plugin = pluginClass.newInstance();
                result.add(service.cast(plugin));
            } catch (Exception e) {
                throw new MockitoConfigurationException(
                        "Failed to load " + service + " using " + resource, e);
            }
        }
        return result;
    }
}
