package org.mockitousage.plugins;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.plugins.AnnotationEngine;
import org.mockito.plugins.InstantiatorProvider;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockitoPlugins;
import org.mockito.plugins.PluginSwitch;
import org.mockito.plugins.StackTraceCleanerProvider;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertNotNull;

public class MockitoPluginsTest extends TestBase {

    private final MockitoPlugins plugins = Mockito.framework().getPlugins();

    @Test public void provides_built_in_plugins() {
        assertNotNull(plugins.getInlineMockMaker());
        assertNotNull(plugins.getDefaultPlugin(MockMaker.class));
        assertNotNull(plugins.getDefaultPlugin(StackTraceCleanerProvider.class));
        assertNotNull(plugins.getDefaultPlugin(PluginSwitch.class));
        assertNotNull(plugins.getDefaultPlugin(InstantiatorProvider.class));
        assertNotNull(plugins.getDefaultPlugin(AnnotationEngine.class));
    }
}
