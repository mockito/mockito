package org.mockitousage.plugins.switcher;

import org.mockito.plugins.PluginSwitch;

import java.util.LinkedList;
import java.util.List;

public class MyPluginSwitch implements PluginSwitch {

    static List<String> invokedFor = new LinkedList<String>();

    public boolean isEnabled(String pluginClassName) {
        invokedFor.add(pluginClassName);
        return true;
    }
}
