package org.mockitousage.plugins.switcher;

import org.mockito.plugins.PluginSwitcher;

import java.util.LinkedList;
import java.util.List;

public class MyPluginSwitcher implements PluginSwitcher {

    static List<String> invokedFor = new LinkedList<String>();

    public boolean isEnabled(String pluginClassName) {
        invokedFor.add(pluginClassName);
        return true;
    }
}
