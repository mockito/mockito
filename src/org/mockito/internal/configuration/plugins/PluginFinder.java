package org.mockito.internal.configuration.plugins;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.io.IOUtil;
import org.mockito.plugins.PluginSwitcher;

import java.io.InputStream;
import java.net.URL;

class PluginFinder {

    private final PluginSwitcher pluginSwitcher;

    public PluginFinder(PluginSwitcher pluginSwitcher) {
        this.pluginSwitcher = pluginSwitcher;
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
                if (!pluginSwitcher.isEnabled(pluginClassName)) {
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
