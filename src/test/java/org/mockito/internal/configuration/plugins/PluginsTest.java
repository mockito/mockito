package org.mockito.internal.configuration.plugins;

import org.junit.Test;
import org.mockito.plugins.MockMaker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PluginsTest {

    @Test
    public void refresh_plugin_registry_reloads_plugins() {
        MockMaker oldMockMaker = Plugins.getMockMaker();
        Plugins.refreshPluginRegistry();
        MockMaker newMockMaker = Plugins.getMockMaker();
        assertNotEquals(oldMockMaker, newMockMaker);
    }

    @Test
    public void plugin_registry_reuses_plugins() {
        MockMaker oldMockMaker = Plugins.getMockMaker();
        MockMaker newMockMaker = Plugins.getMockMaker();
        assertEquals(oldMockMaker, newMockMaker);
    }
}
