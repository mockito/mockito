package org.mockito.internal.configuration.plugins;

import org.mockito.plugins.PluginSwitcher;

class DefaultPluginSwitcher implements PluginSwitcher {
    public boolean isEnabled(String pluginClassName) {
        return true;
    }
}
