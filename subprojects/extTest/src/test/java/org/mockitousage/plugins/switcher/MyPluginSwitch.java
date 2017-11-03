/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
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
