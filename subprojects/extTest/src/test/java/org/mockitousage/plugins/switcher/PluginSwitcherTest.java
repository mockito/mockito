/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.plugins.switcher;

import org.junit.Test;
import org.mockitousage.plugins.stacktrace.MyStackTraceCleanerProvider;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

//TODO consider adding a separate source set or project
//that validates that switcher can disable a plugin
public class PluginSwitcherTest {
    
    @Test
    public void plugin_switcher_is_used() {
        mock(List.class);
        assertEquals(MyPluginSwitcher.invokedFor, asList(MyStackTraceCleanerProvider.class.getName()));
    }
}
