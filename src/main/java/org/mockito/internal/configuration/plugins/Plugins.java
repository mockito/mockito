/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.plugins;

import org.mockito.plugins.MockitoLogger;
import org.mockito.plugins.AnnotationEngine;
import org.mockito.plugins.InstantiatorProvider2;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockitoPlugins;
import org.mockito.plugins.StackTraceCleanerProvider;

/**
 * Access to Mockito behavior that can be reconfigured by plugins
 */
public class Plugins {

    private static final PluginRegistry registry = new PluginRegistry();

    /**
     * The implementation of the stack trace cleaner
     */
    public static StackTraceCleanerProvider getStackTraceCleanerProvider() {
        return registry.getStackTraceCleanerProvider();
    }

    /**
     * Returns the implementation of the mock maker available for the current runtime.
     *
     * <p>Returns default mock maker if no
     * {@link org.mockito.plugins.MockMaker} extension exists or is visible in the current classpath.</p>
     */
    public static MockMaker getMockMaker() {
        return registry.getMockMaker();
    }

    /**
     * Returns the instantiator provider available for the current runtime.
     *
     * <p>Returns {@link org.mockito.internal.creation.instance.DefaultInstantiatorProvider} if no
     * {@link org.mockito.plugins.InstantiatorProvider2} extension exists or is visible in the
     * current classpath.</p>
     */
    public static InstantiatorProvider2 getInstantiatorProvider() {
      return registry.getInstantiatorProvider();
    }

    /**
     * Returns the annotation engine available for the current runtime.
     *
     * <p>Returns {@link org.mockito.internal.configuration.InjectingAnnotationEngine} if no
     * {@link org.mockito.plugins.AnnotationEngine} extension exists or is visible in the current classpath.</p>
     */
    public static AnnotationEngine getAnnotationEngine() {
        return registry.getAnnotationEngine();
    }

    /**
     * Returns the logger available for the current runtime.
     *
     * <p>Returns {@link org.mockito.internal.util.ConsoleMockitoLogger} if no
     * {@link org.mockito.plugins.MockitoLogger} extension exists or is visible in the current classpath.</p>
     */
    public static MockitoLogger getMockitoLogger() {
        return registry.getMockitoLogger();
    }

    /**
     * @return instance of mockito plugins type
     */
    public static MockitoPlugins getPlugins() {
        return new DefaultMockitoPlugins();
    }
}
