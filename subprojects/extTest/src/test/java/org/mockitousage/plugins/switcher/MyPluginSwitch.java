/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.plugins.switcher;

import java.util.ArrayList;
import java.util.List;

import org.mockito.plugins.PluginSwitch;

public class MyPluginSwitch implements PluginSwitch {

    static List<String> invokedFor = new ArrayList<>();

    public boolean isEnabled(String pluginClassName) {
        invokedFor.add(pluginClassName);
        return true;
    }
}
