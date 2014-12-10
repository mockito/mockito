package org.mockito.internal.configuration.plugins;

import org.mockito.plugins.PluginSwitch;

class DefaultPluginSwitch implements PluginSwitch {
    public boolean isEnabled(String pluginClassName) {
        return true;
    }
}
