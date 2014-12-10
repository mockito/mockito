package org.mockitousage.plugins.switcher;

import org.mockito.plugins.PluginSwitcher;

import java.util.LinkedList;
import java.util.List;

public class MyPluginSwitcher implements PluginSwitcher {

    static List<Class> invokedFor = new LinkedList<Class>();

    public boolean isEnabled(Class pluginClass) {
        invokedFor.add(pluginClass);
        return true;
    }
}
