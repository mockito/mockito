/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.plugins;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.when;

public class PluginLoaderTest {

    @Rule public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

    @Mock PluginInitializer initializer;
    @Mock DefaultMockitoPlugins plugins;
    @InjectMocks PluginLoader loader;

    @Test
    public void loads_plugin() {
        when(initializer.loadImpl(FooPlugin.class)).thenReturn(new FooPlugin());

        //when
        FooPlugin plugin = loader.loadPlugin(FooPlugin.class);

        //then
        assertNotNull(plugin);
    }

    @Test
    public void loads_alternative_plugin() {
        willReturn(null).given(initializer).loadImpl(FooPlugin.class);
        BarPlugin expected = new BarPlugin();
        willReturn(expected).given(initializer).loadImpl(BarPlugin.class);

        //when
        Object plugin = loader.loadPlugin(FooPlugin.class, BarPlugin.class);

        //then
        assertSame(plugin, expected);
    }

    @Test
    public void loads_default_plugin() {
        willReturn(null).given(initializer).loadImpl(FooPlugin.class);
        willReturn(null).given(initializer).loadImpl(BarPlugin.class);
        FooPlugin expected = new FooPlugin();
        willReturn(expected).given(plugins).getDefaultPlugin(FooPlugin.class);

        //when
        Object plugin = loader.loadPlugin(FooPlugin.class, BarPlugin.class);

        //then
        assertSame(plugin, expected);
    }

    @Test
    public void fails_to_load_plugin() {
        RuntimeException cause = new RuntimeException("Boo!");
        when(initializer.loadImpl(Foo.class)).thenThrow(cause);

        //when
        final Foo plugin = loader.loadPlugin(Foo.class);

        //then
        Assertions.assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                plugin.toString(); //call any method on the plugin
            }
        }).isInstanceOf(IllegalStateException.class)
            .hasMessage("Could not initialize plugin: interface org.mockito.internal.configuration.plugins.PluginLoaderTest$Foo (alternate: null)")
            .hasCause(cause);
    }

    static class FooPlugin {}
    static class BarPlugin {}
    static interface Foo {}
}
