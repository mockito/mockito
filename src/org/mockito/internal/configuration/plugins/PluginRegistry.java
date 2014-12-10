package org.mockito.internal.configuration.plugins;

import org.mockito.plugins.MockMaker;
import org.mockito.plugins.PluginSwitcher;
import org.mockito.plugins.StackTraceCleanerProvider;

class PluginRegistry {

    private final PluginSwitcher pluginSwitcher
            = new PluginLoader(new DefaultPluginSwitcher()).loadPlugin(PluginSwitcher.class, DefaultPluginSwitcher.class.getName());

    private final MockMaker mockMaker
            = new PluginLoader(pluginSwitcher).loadPlugin(MockMaker.class, "org.mockito.internal.creation.cglib.CglibMockMaker");

    private final StackTraceCleanerProvider stackTraceCleanerProvider
            = new PluginLoader(pluginSwitcher).loadPlugin(StackTraceCleanerProvider.class, "org.mockito.internal.exceptions.stacktrace.DefaultStackTraceCleanerProvider");

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
