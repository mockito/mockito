package org.mockito.internal.configuration.plugins;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.io.IOUtil;
import org.mockito.plugins.PluginSwitch;

import java.io.InputStream;
import java.net.URL;

class PluginFinder {

    private final PluginSwitch pluginSwitch;

    public PluginFinder(PluginSwitch pluginSwitch) {
        this.pluginSwitch = pluginSwitch;
    }

    String findPluginClass(Iterable<URL> resources) {
        for (URL resource : resources) {
            InputStream s = null;
            try {
                s = resource.openStream();
                String pluginClassName = new PluginFileReader().readPluginClass(s);
                if (pluginClassName == null) {
                    //For backwards compatibility
                    //If the resource does not have plugin class name we're ignoring it
                    continue;
                }
                if (!pluginSwitch.isEnabled(pluginClassName)) {
                    continue;
                }
                return pluginClassName;
            } catch(Exception e) {
                throw new MockitoException("Problems reading plugin implementation from: " + resource, e);
            } finally {
                IOUtil.closeQuietly(s);
            }
        }
        return null;
    }
}
