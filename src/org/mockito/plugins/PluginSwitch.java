package org.mockito.plugins;

import org.mockito.Incubating;

/**
 * Allows switching off the plugins that are discovered on classpath.
 *
 * <p>
 *     Mockito will invoke this interface in order to select whether a plugin is enabled or not.
 * </p>
 *
 * <p>
 *     When a particular plugin is switched off, the default Mockito behavior is used.
 *     For example, if Android's dexmaker MockMaker is switched off,
 *     Mockito default MockMaker implementation is used {@link org.mockito.plugins.MockMaker}
 * </p>
 *
 * <h3>Using the extension point</h3>
 *
 * <p>
 *     The plugin mechanism of mockito works in a similar way as the {@link java.util.ServiceLoader}, however instead of
 *     looking in the <code>META-INF</code> directory, Mockito will look in <code>mockito-extensions</code> directory.
 *     <em>The reason for that is that Android SDK strips jar from the <code>META-INF</code> directory when creating an APK.</em>
 * </p>
 *
 * <ol style="list-style-type: lower-alpha">
 *     <li>The implementation itself, for example <code>org.awesome.mockito.AwesomeMockMaker</code> that extends the <code>MockMaker</code>.</li>
 *     <li>A file "<code>mockito-extensions/org.mockito.plugins.MockMaker</code>". The content of this file is
 *     exactly a <strong>one</strong> line with the qualified name: <code>org.awesome.mockito.AwesomeMockMaker</code>.</li>
 * </ol></p>
 *
 * <p>Note that if several <code>mockito-extensions/org.mockito.plugins.MockMaker</code> files exists in the classpath
 * Mockito will only use the first returned by the standard {@link ClassLoader#getResource} mechanism.
 * <p>
 *     So just create a custom implementation of {@link PluginSwitch} and place the qualified name in the following file
 *     <code>mockito-extensions/org.mockito.plugins.PluginSwitch</code>.
 * </p>
 *
 * @since 1.10.15
 */
@Incubating
public interface PluginSwitch {

    /**
     * Mockito invokes this method for every plugin found in the classpath
     * (except from the {@code PluginSwitch} implementation itself).
     * If no custom plugins are discovered this method is not invoked.
     *
     * @since 1.10.15
     */
    boolean isEnabled(String pluginClassName);
}
