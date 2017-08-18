/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.plugins;

import org.mockito.plugins.AnnotationEngine;
import org.mockito.plugins.InstantiatorProvider;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.PluginSwitch;
import org.mockito.plugins.StackTraceCleanerProvider;

class PluginRegistry {

    private final PluginSwitch pluginSwitch = new PluginLoader(new DefaultPluginSwitch())
            .loadPlugin(PluginSwitch.class);

    private final MockMaker mockMaker = new PluginLoader(pluginSwitch)
            .withAlias(DefaultMockitoPlugins.INLINE_ALIAS)
            .loadPlugin(MockMaker.class);

    private final StackTraceCleanerProvider stackTraceCleanerProvider = new PluginLoader(pluginSwitch)
            .loadPlugin(StackTraceCleanerProvider.class);

    private final InstantiatorProvider instantiatorProvider = new PluginLoader(pluginSwitch)
            .loadPlugin(InstantiatorProvider.class);

    private AnnotationEngine annotationEngine = new PluginLoader(pluginSwitch)
            .loadPlugin(AnnotationEngine.class);

    /**
     * The implementation of the stack trace cleaner
     */
    StackTraceCleanerProvider getStackTraceCleanerProvider() {
        //TODO we should throw some sensible exception if this is null.
        return stackTraceCleanerProvider;
    }

    /**
     * Returns the implementation of the mock maker available for the current runtime.
     *
     * <p>Returns {@link org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker} if no
     * {@link org.mockito.plugins.MockMaker} extension exists or is visible in the current classpath.</p>
     */
    MockMaker getMockMaker() {
        return mockMaker;
    }

    /**
     * Returns the instantiator provider available for the current runtime.
     *
     * <p>Returns {@link org.mockito.internal.creation.instance.DefaultInstantiatorProvider} if no
     * {@link org.mockito.plugins.InstantiatorProvider} extension exists or is visible in the current classpath.</p>
     */
    InstantiatorProvider getInstantiatorProvider() {
      return instantiatorProvider;
    }

    /**
     * Returns the annotation engine available for the current runtime.
     *
     * <p>Returns {@link org.mockito.internal.configuration.InjectingAnnotationEngine} if no
     * {@link org.mockito.plugins.AnnotationEngine} extension exists or is visible in the current classpath.</p>
     */
    AnnotationEngine getAnnotationEngine() {
        return annotationEngine;
    }
}
