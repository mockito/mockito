/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.plugins;

import static org.junit.Assert.*;
import static org.mockito.internal.configuration.plugins.DefaultMockitoPlugins.INLINE_ALIAS;
import static org.mockito.internal.configuration.plugins.DefaultMockitoPlugins.PROXY_ALIAS;
import static org.mockito.internal.configuration.plugins.DefaultMockitoPlugins.SUBCLASS_ALIAS;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.MockMakers;
import org.mockito.Mockito;
import org.mockito.internal.creation.bytebuddy.InlineByteBuddyMockMaker;
import org.mockito.internal.util.ConsoleMockitoLogger;
import org.mockito.plugins.InstantiatorProvider2;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockitoLogger;
import org.mockito.plugins.MockitoPlugins;
import org.mockitoutil.TestBase;

public class DefaultMockitoPluginsTest extends TestBase {

    private final DefaultMockitoPlugins plugins = new DefaultMockitoPlugins();

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

    @Test
    public void test_getMockMaker() {
        assertNotNull(plugins.getMockMaker(null));
        assertTrue(plugins.getMockMaker(MockMakers.INLINE) instanceof InlineByteBuddyMockMaker);
    }

    @Test
    public void test_getMockMaker_throws_IllegalStateException_on_invalid_name() {
        Assertions.assertThatThrownBy(
                        () -> {
                            plugins.getMockMaker("non existing");
                        })
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Failed to load MockMaker: non existing");
    }

    @Test
    public void
            test_MockitoPlugins_getMockMaker_default_method_throws_UnsupportedOperationException() {
        MockitoPlugins pluginsSpy = Mockito.spy(MockitoPlugins.class);
        Assertions.assertThatThrownBy(
                        () -> {
                            pluginsSpy.getMockMaker("non existing");
                        })
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("This method is not implemented.");
    }
}
