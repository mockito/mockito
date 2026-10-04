/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.plugins;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.io.IOUtil;
import org.mockito.plugins.PluginSwitch;

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
                    // For backwards compatibility
                    // If the resource does not have plugin class name we're ignoring it
                    continue;
                }
                if (!pluginSwitch.isEnabled(pluginClassName)) {
                    continue;
                }
                return pluginClassName;
            } catch (Exception e) {
                throw new MockitoException(
                        "Problems reading plugin implementation from: " + resource, e);
            } finally {
                IOUtil.closeQuietly(s);
            }
        }
        return null;
    }

    List<String> findPluginClasses(Iterable<URL> resources) {
        Set<String> pluginClassNames = new LinkedHashSet<>();
        for (URL resource : resources) {
            InputStream s = null;
            try {
                s = resource.openStream();
                // TODO: does this count as an api change?  Previously: multiple meta-inf-like files
                //  could each contain multiple classes, but only the first class from each file would be
                //  loaded.  Now all classes from each file will be loaded.  This could either be a break
                //  or a fix.
                //  This currently only impacts the MockResolver plugin, which this code was originally for.
                List<String> pluginClassNamesFromFile = new PluginFileReader().readPluginClasses(s);
                if (pluginClassNamesFromFile.isEmpty()) {
                    // For backwards compatibility
                    // If the resource does not have plugin class name we're ignoring it
                    continue;
                }
                pluginClassNamesFromFile.removeIf(name -> !pluginSwitch.isEnabled(name));
                pluginClassNames.addAll(pluginClassNamesFromFile);
            } catch (Exception e) {
                throw new MockitoException(
                        "Problems reading plugin implementation from: " + resource, e);
            } finally {
                IOUtil.closeQuietly(s);
            }
        }
        return new ArrayList<>(pluginClassNames);
    }
}
