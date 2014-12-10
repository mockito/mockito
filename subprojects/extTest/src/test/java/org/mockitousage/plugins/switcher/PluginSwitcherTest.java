/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.plugins.switcher;

import org.junit.Test;
import org.mockitousage.plugins.stacktrace.MyStackTraceCleanerProvider;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

//TODO consider adding a separate source set or project
//that validates that switcher can disable a plugin
public class PluginSwitcherTest {
    
    @Test
    public void plugin_switcher_is_used() {
        assertEquals(MyPluginSwitcher.invokedFor, asList(MyStackTraceCleanerProvider.class));
    }
}
