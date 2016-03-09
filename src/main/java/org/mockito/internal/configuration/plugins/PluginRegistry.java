package org.mockito.internal.configuration.plugins;

import org.mockito.plugins.InstantiatorProvider;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.PluginSwitch;
import org.mockito.plugins.StackTraceCleanerProvider;

class PluginRegistry {

    private final PluginSwitch pluginSwitch
            = new PluginLoader(new DefaultPluginSwitch()).loadPlugin(PluginSwitch.class, DefaultPluginSwitch.class.getName());

    private final MockMaker mockMaker
            = new PluginLoader(pluginSwitch).loadPlugin(MockMaker.class, "org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker");

    private final StackTraceCleanerProvider stackTraceCleanerProvider
            = new PluginLoader(pluginSwitch).loadPlugin(StackTraceCleanerProvider.class, "org.mockito.internal.exceptions.stacktrace.DefaultStackTraceCleanerProvider");

    private final InstantiatorProvider instantiatorProvider
            = new PluginLoader(pluginSwitch).loadPlugin(InstantiatorProvider.class, "org.mockito.internal.creation.instance.DefaultInstantiatorProvider");

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
}
