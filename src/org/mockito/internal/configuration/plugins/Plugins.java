package org.mockito.internal.configuration.plugins;

import org.mockito.plugins.MockMaker;
import org.mockito.plugins.StackTraceCleanerProvider;

/**
 * Access to Mockito behavior that can be reconfigured by plugins
 */
public class Plugins {

    private static PluginRegistry registry = new PluginRegistry();

    /**
     * The implementation of the stack trace cleaner
     */
    public static StackTraceCleanerProvider getStackTraceCleanerProvider() {
        return registry.getStackTraceCleanerProvider();
    }

    /**
     * Returns the implementation of the mock maker available for the current runtime.
     *
     * <p>Returns {@link org.mockito.internal.creation.cglib.CglibMockMaker} if no
     * {@link org.mockito.plugins.MockMaker} extension exists or is visible in the current classpath.</p>
     */
    public static MockMaker getMockMaker() {
        return registry.getMockMaker();
    }
}
