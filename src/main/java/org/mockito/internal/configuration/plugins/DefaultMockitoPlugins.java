/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.plugins;

import org.mockito.internal.creation.instance.InstantiatorProvider2Adapter;
import org.mockito.plugins.AnnotationEngine;
import org.mockito.plugins.InstantiatorProvider;
import org.mockito.plugins.InstantiatorProvider2;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockitoLogger;
import org.mockito.plugins.MockitoPlugins;
import org.mockito.plugins.PluginSwitch;
import org.mockito.plugins.StackTraceCleanerProvider;

import java.util.HashMap;
import java.util.Map;

class DefaultMockitoPlugins implements MockitoPlugins {

    private final static Map<String, String> DEFAULT_PLUGINS = new HashMap<String, String>();
    static final String INLINE_ALIAS = "mock-maker-inline";

    static {
        //Keep the mapping: plugin interface name -> plugin implementation class name
        DEFAULT_PLUGINS.put(PluginSwitch.class.getName(), DefaultPluginSwitch.class.getName());
        DEFAULT_PLUGINS.put(MockMaker.class.getName(), "org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker");
        DEFAULT_PLUGINS.put(StackTraceCleanerProvider.class.getName(), "org.mockito.internal.exceptions.stacktrace.DefaultStackTraceCleanerProvider");
        DEFAULT_PLUGINS.put(InstantiatorProvider2.class.getName(), "org.mockito.internal.creation.instance.DefaultInstantiatorProvider");
        DEFAULT_PLUGINS.put(AnnotationEngine.class.getName(), "org.mockito.internal.configuration.InjectingAnnotationEngine");
        DEFAULT_PLUGINS.put(INLINE_ALIAS, "org.mockito.internal.creation.bytebuddy.InlineByteBuddyMockMaker");
        DEFAULT_PLUGINS.put(MockitoLogger.class.getName(), "org.mockito.internal.util.ConsoleMockitoLogger");
    }

    @Override
    public <T> T getDefaultPlugin(Class<T> pluginType) {
        if (pluginType == InstantiatorProvider.class) {
            //the implementation class is not configured via map so that we can reduce duplication
            //(ensure that we are adapting the currently configured default implementation for InstantiatorProvider2)
            String className = DEFAULT_PLUGINS.get(InstantiatorProvider2.class.getName());
            return pluginType.cast(new InstantiatorProvider2Adapter(create(InstantiatorProvider2.class, className)));
        } else {
            String className = DEFAULT_PLUGINS.get(pluginType.getName());
            return create(pluginType, className);
        }
    }

    String getDefaultPluginClass(String classOrAlias) {
        return DEFAULT_PLUGINS.get(classOrAlias);
    }

    /**
     * Creates an instance of given plugin type, using specific implementation class.
     */
    private <T> T create(Class<T> pluginType, String className) {
        if (className == null) {
            throw new IllegalStateException(
                "No default implementation for requested Mockito plugin type: " + pluginType.getName() + "\n"
                    + "Is this a valid Mockito plugin type? If yes, please report this problem to Mockito team.\n"
                    + "Otherwise, please check if you are passing valid plugin type.\n"
                    + "Examples of valid plugin types: MockMaker, StackTraceCleanerProvider.");
        }
        try {
            // Default implementation. Use our own ClassLoader instead of the context
            // ClassLoader, as the default implementation is assumed to be part of
            // Mockito and may not be available via the context ClassLoader.
            return pluginType.cast(Class.forName(className).newInstance());
        } catch (Exception e) {
            throw new IllegalStateException("Internal problem occurred, please report it. " +
                "Mockito is unable to load the default implementation of class that is a part of Mockito distribution. " +
                "Failed to load " + pluginType, e);
        }
    }

    @Override
    public MockMaker getInlineMockMaker() {
        return create(MockMaker.class, DEFAULT_PLUGINS.get(INLINE_ALIAS));
    }
}
