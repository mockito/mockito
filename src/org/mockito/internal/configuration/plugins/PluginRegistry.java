package org.mockito.internal.configuration.plugins;

import org.mockito.plugins.MockMaker;
import org.mockito.plugins.PluginSwitch;
import org.mockito.plugins.StackTraceCleanerProvider;

class PluginRegistry {

    private final PluginSwitch pluginSwitch
            = new PluginLoader(new DefaultPluginSwitch()).loadPlugin(PluginSwitch.class, DefaultPluginSwitch.class.getName());

    private final MockMaker mockMaker
            = new PluginLoader(pluginSwitch).loadPlugin(MockMaker.class, "org.mockito.internal.creation.cglib.CglibMockMaker");

    private final StackTraceCleanerProvider stackTraceCleanerProvider
            = new PluginLoader(pluginSwitch).loadPlugin(StackTraceCleanerProvider.class, "org.mockito.internal.exceptions.stacktrace.DefaultStackTraceCleanerProvider");

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
     * <p>Returns {@link org.mockito.internal.creation.cglib.CglibMockMaker} if no
     * {@link org.mockito.plugins.MockMaker} extension exists or is visible in the current classpath.</p>
     */
    MockMaker getMockMaker() {
        return mockMaker;
    }
}
