package org.mockito.internal.configuration.plugins;

import org.junit.Before;
import org.junit.Test;
import org.mockitoutil.ClassLoaders;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test PluginLoader. This test check that loaded plugin can be assigned to fields of classes what loaded by
 * application class loader
 */
public class PluginLoaderTest {

    private static final String PLUGIN_RESOURCE_CONTENT = PluginImpl.class.getName();
    private static final String PLUGIN_RESOURCE_PATH = "mockito-extensions/org.mockito.internal.configuration.plugins.PluginInterface";
    private PluginLoader pluginLoader;

    @Before
    public void setUp() {
        pluginLoader = new PluginLoader(new DefaultPluginSwitch());
    }

    @Test
    public void should_load_custom_plugin_without_context_classloader() {

        Object pluginInterface = loadPlugin();

        assertThatCustomPluginLoaded(pluginInterface);
    }


    @Test
    public void should_load_custom_plugin_with_custom_context_classloader() throws Exception {

        ClassLoader classLoader = ClassLoaders.inMemoryClassLoader()
                                              .withClassDefinition(PLUGIN_RESOURCE_PATH, PLUGIN_RESOURCE_CONTENT.getBytes("UTF-8"))
                                              .build();

        Thread.currentThread().setContextClassLoader(classLoader);

        Object plugin = loadPlugin();

        assertThatCustomPluginLoaded(plugin);
    }

    @Test
    public void should_load_default_plugin_with_custom_context_classloader() throws Exception {

        ClassLoader classLoader = ClassLoaders.inMemoryClassLoader().build();

        Thread.currentThread().setContextClassLoader(classLoader);

        Object plugin = loadPlugin();

        assertThatDefaultPluginLoaded(plugin);
    }

    private Object loadPlugin() {
        return pluginLoader.loadPlugin(PluginInterface.class, DefaultPlugin.class.getName());
    }

    private void assertThatCustomPluginLoaded(Object plugin) {

        assertThat(plugin.getClass())
                .describedAs("Loaded plugin class '%s' is not assignable to local class '%s'", plugin.getClass(), PluginImpl.class)
                .isAssignableFrom(PluginImpl.class);
    }

    private void assertThatDefaultPluginLoaded(Object plugin) {
        assertThat(plugin.getClass())
                .describedAs("Loaded plugin class '%s' is not assignable to  default plugin local class '%s'", plugin.getClass(), DefaultPlugin.class)
                .isAssignableFrom(DefaultPlugin.class);
    }


}
