package org.mockito.internal.configuration.plugins;

import org.mockito.plugins.InstantiatorProvider;
import org.mockito.plugins.MockMaker;
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
     * {@link org.mockito.plugins.InstantiatorProvider} extension exists or is visible in the current classpath.</p>
     */
    public static InstantiatorProvider getInstantiatorProvider() {
      return registry.getInstantiatorProvider();
    }
}
