/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.plugins;

import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

public class PluginLoaderTest {

    @Rule public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

    @Mock PluginInitializer initializer;
    @Mock DefaultMockitoPlugins plugins;
    @InjectMocks PluginLoader loader;

    @Test
    public void loads_plugin() {
        FooPlugin expected = new FooPlugin();
        when(initializer.loadImpl(FooPlugin.class)).thenReturn(expected);

        // when
        FooPlugin plugin = loader.loadPlugin(FooPlugin.class);

        // then
        assertSame(expected, plugin);
    }

    @Test
    public void loads_preferred_plugin() {
        FooPlugin expected = new FooPlugin();
        willReturn(expected).given(initializer).loadImpl(FooPlugin.class);

        // when
        Object plugin = loader.loadPlugin(FooPlugin.class, BarPlugin.class);

        // then
        assertSame(expected, plugin);
        verify(initializer, never()).loadImpl(BarPlugin.class);
    }

    @Test
    public void loads_alternative_plugin() {
        willReturn(null).given(initializer).loadImpl(FooPlugin.class);
        BarPlugin expected = new BarPlugin();
        willReturn(expected).given(initializer).loadImpl(BarPlugin.class);

        // when
        Object plugin = loader.loadPlugin(FooPlugin.class, BarPlugin.class);

        // then
        assertSame(expected, plugin);
    }

    @Test
    public void loads_default_plugin() {
        willReturn(null).given(initializer).loadImpl(FooPlugin.class);
        willReturn(null).given(initializer).loadImpl(BarPlugin.class);
        FooPlugin expected = new FooPlugin();
        willReturn(expected).given(plugins).getDefaultPlugin(FooPlugin.class);

        // when
        Object plugin = loader.loadPlugin(FooPlugin.class, BarPlugin.class);

        // then
        assertSame(expected, plugin);
    }

    @Test
    public void fails_to_load_plugin() {
        RuntimeException cause = new RuntimeException("Boo!");
        when(initializer.loadImpl(Foo.class)).thenThrow(cause);

        // when
        final Foo plugin = loader.loadPlugin(Foo.class);

        // then
        Assertions.assertThatThrownBy(
                        new ThrowableAssert.ThrowingCallable() {
                            @Override
                            public void call() throws Throwable {
                                plugin.toString(); // call any method on the plugin
                            }
                        })
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(
                        "Could not initialize plugin: interface org.mockito.internal.configuration.plugins.PluginLoaderTest$Foo (alternate: null)")
                .hasCause(cause);
    }

    @Test
    public void loads_preferred_plugin_inheritance() {
        FooChildPlugin expected = new FooChildPlugin();
        willReturn(expected).given(initializer).loadImpl(Foo.class);

        // when
        Foo plugin = loader.loadPlugin(Foo.class, FooChildPlugin.class);

        // then
        assertSame(expected, plugin);
        verify(initializer, never()).loadImpl(FooChildPlugin.class);
    }

    @Test
    public void loads_alternative_plugin_inheritance() {
        willReturn(null).given(initializer).loadImpl(Bar.class);
        BarChildPlugin expected = new BarChildPlugin();
        willReturn(expected).given(initializer).loadImpl(BarChildPlugin.class);

        // when
        Bar plugin = loader.loadPlugin(Bar.class, BarChildPlugin.class);

        // then
        assertSame(expected, plugin);
    }

    @Test
    public void loads_default_plugin_inheritance() {
        willReturn(null).given(initializer).loadImpl(Foo.class);
        willReturn(null).given(initializer).loadImpl(FooChildPlugin.class);
        FooChildPlugin expected = new FooChildPlugin();
        willReturn(expected).given(plugins).getDefaultPlugin(Foo.class);

        // when
        Foo plugin = loader.loadPlugin(Foo.class, FooChildPlugin.class);

        // then
        assertSame(expected, plugin);
    }

    @Test
    public void loads_preferred_plugin_inheritance_reversed() {
        FooChildPlugin expected = new FooChildPlugin();
        willReturn(expected).given(initializer).loadImpl(FooChildPlugin.class);

        // when
        Foo plugin = loader.loadPlugin(FooChildPlugin.class, Foo.class);

        // then
        assertSame(expected, plugin);
        verify(initializer, never()).loadImpl(Foo.class);
    }

    @Test
    public void loads_alternative_plugin_inheritance_reversed() {
        willReturn(null).given(initializer).loadImpl(BarChildPlugin.class);
        BarChildPlugin expected = new BarChildPlugin();
        willReturn(expected).given(initializer).loadImpl(Bar.class);

        // when
        Bar plugin = loader.loadPlugin(BarChildPlugin.class, Bar.class);

        // then
        assertSame(expected, plugin);
    }

    @Test
    public void loads_default_plugin_inheritance_reversed() {
        willReturn(null).given(initializer).loadImpl(Foo.class);
        willReturn(null).given(initializer).loadImpl(FooChildPlugin.class);
        FooChildPlugin expected = new FooChildPlugin();
        willReturn(expected).given(plugins).getDefaultPlugin(FooChildPlugin.class);

        // when
        Foo plugin = loader.loadPlugin(FooChildPlugin.class, Foo.class);

        // then
        assertSame(expected, plugin);
    }

    @Test
    public void loads_preferred_plugin_inheritance_lowest_common_denominator() {
        FooBarChildPlugin1 expected = new FooBarChildPlugin1();
        willReturn(expected).given(initializer).loadImpl(FooBarChildPlugin1.class);

        // when
        FooBar plugin = loader.loadPlugin(FooBarChildPlugin1.class, FooBarChildPlugin2.class);

        // then
        assertSame(expected, plugin);
        verify(initializer, never()).loadImpl(FooBarChildPlugin2.class);
    }

    @Test
    public void loads_alternative_plugin_inheritance_lowest_common_denominator() {
        willReturn(null).given(initializer).loadImpl(FooBarChildPlugin1.class);
        FooBarChildPlugin2 expected = new FooBarChildPlugin2();
        willReturn(expected).given(initializer).loadImpl(FooBarChildPlugin2.class);

        // when
        FooBar plugin = loader.loadPlugin(FooBarChildPlugin1.class, FooBarChildPlugin2.class);

        // then
        assertSame(expected, plugin);
    }

    @Test
    public void loads_default_plugin_inheritance_lowest_common_denominator() {
        willReturn(null).given(initializer).loadImpl(FooBarChildPlugin1.class);
        willReturn(null).given(initializer).loadImpl(FooBarChildPlugin2.class);
        FooBarChildPlugin1 expected = new FooBarChildPlugin1();
        willReturn(expected).given(plugins).getDefaultPlugin(FooBarChildPlugin1.class);

        // when
        FooBar plugin = loader.loadPlugin(FooBarChildPlugin1.class, FooBarChildPlugin2.class);

        // then
        assertSame(expected, plugin);
    }

    static class FooPlugin {}

    static class BarPlugin {}

    interface Foo {}

    interface Bar {}

    static class BarChildPlugin implements Bar {}

    static class FooChildPlugin implements Foo {}

    interface FooBar {}

    static class FooBarChildPlugin1 implements FooBar {}

    static class FooBarChildPlugin2 implements FooBar {}
}
