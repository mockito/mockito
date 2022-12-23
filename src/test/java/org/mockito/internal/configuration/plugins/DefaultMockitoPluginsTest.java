/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.plugins;

import static org.junit.Assert.*;
import static org.mockito.internal.configuration.plugins.DefaultMockitoPlugins.INLINE_ALIAS;
import static org.mockito.internal.configuration.plugins.DefaultMockitoPlugins.PROXY_ALIAS;
import static org.mockito.internal.configuration.plugins.DefaultMockitoPlugins.SUBCLASS_ALIAS;

import org.junit.Test;
import org.mockito.internal.creation.bytebuddy.InlineByteBuddyMockMaker;
import org.mockito.internal.util.ConsoleMockitoLogger;
import org.mockito.plugins.InstantiatorProvider2;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockitoLogger;
import org.mockitoutil.TestBase;

public class DefaultMockitoPluginsTest extends TestBase {

    private DefaultMockitoPlugins plugins = new DefaultMockitoPlugins();

    @Test
    public void provides_plugins() throws Exception {
        assertEquals(
                "org.mockito.internal.creation.bytebuddy.InlineByteBuddyMockMaker",
                DefaultMockitoPlugins.getDefaultPluginClass(INLINE_ALIAS));
        assertEquals(InlineByteBuddyMockMaker.class, plugins.getInlineMockMaker().getClass());
        assertEquals(
                "org.mockito.internal.creation.proxy.ProxyMockMaker",
                DefaultMockitoPlugins.getDefaultPluginClass(PROXY_ALIAS));
        assertEquals(
                "org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker",
                DefaultMockitoPlugins.getDefaultPluginClass(SUBCLASS_ALIAS));
        assertEquals(
                InlineByteBuddyMockMaker.class,
                plugins.getDefaultPlugin(MockMaker.class).getClass());
        assertNotNull(plugins.getDefaultPlugin(InstantiatorProvider2.class));
        assertEquals(
                ConsoleMockitoLogger.class,
                plugins.getDefaultPlugin(MockitoLogger.class).getClass());
    }
}
