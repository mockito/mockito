package org.mockito.plugins;

/**
 * Allows switching off the plugins that are discovered on classpath.
 * When a particular plugin is switched off, the default Mockito behavior is used.
 * For example, if Android's dexmaker MockMaker is switched off,
 * Mockito default MockMaker implementation is used {@link org.mockito.plugins.MockMaker}
 */
public interface PluginSwitch {

    /**
     * Mockito invokes this method for every plugin found in the classpath
     * (except from the {@code PluginSwitch} implementation itself).
     * If no custom plugins are discovered this method is not invoked.
     */
    boolean isEnabled(String pluginClassName);
}