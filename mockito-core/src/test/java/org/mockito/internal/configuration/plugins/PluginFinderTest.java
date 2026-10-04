/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.plugins;

import static java.util.Arrays.asList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.io.IOUtil;
import org.mockito.plugins.PluginSwitch;
import org.mockitoutil.TestBase;

public class PluginFinderTest extends TestBase {

    @Mock PluginSwitch switcher;
    @InjectMocks PluginFinder finder;
    public @Rule TemporaryFolder tmp = new TemporaryFolder();

    @Test
    public void empty_resources() {
        assertNull(finder.findPluginClass(Collections.<URL>emptyList()));
    }

    @Test
    public void no_valid_impl() throws Exception {
        File f = tmp.newFile();

        // when
        IOUtil.writeText("  \n  ", f);

        // then
        assertNull(finder.findPluginClass(asList(f.toURI().toURL())));
    }

    @Test
    public void single_implementation() throws Exception {
        File f = tmp.newFile();
        when(switcher.isEnabled("foo.Foo")).thenReturn(true);

        // when
        IOUtil.writeText("  foo.Foo  ", f);

        // then
        assertEquals("foo.Foo", finder.findPluginClass(asList(f.toURI().toURL())));
    }

    @Test
    public void single_implementation_disabled() throws Exception {
        File f = tmp.newFile();
        when(switcher.isEnabled("foo.Foo")).thenReturn(false);

        // when
        IOUtil.writeText("  foo.Foo  ", f);

        // then
        assertEquals(null, finder.findPluginClass(asList(f.toURI().toURL())));
    }

    @Test
    public void multiple_implementations_only_one_enabled() throws Exception {
        File f1 = tmp.newFile();
        File f2 = tmp.newFile();

        when(switcher.isEnabled("Bar")).thenReturn(true);

        // when
        IOUtil.writeText("Foo", f1);
        IOUtil.writeText("Bar", f2);

        // then
        assertEquals("Bar", finder.findPluginClass(asList(f1.toURI().toURL(), f2.toURI().toURL())));
    }

    @Test
    public void multiple_implementations_only_one_useful() throws Exception {
        File f1 = tmp.newFile();
        File f2 = tmp.newFile();

        when(switcher.isEnabled(anyString())).thenReturn(true);

        // when
        IOUtil.writeText("   ", f1);
        IOUtil.writeText("X", f2);

        // then
        assertEquals("X", finder.findPluginClass(asList(f1.toURI().toURL(), f2.toURI().toURL())));
    }

    @Test
    public void multiple_empty_implementations() throws Exception {
        File f1 = tmp.newFile();
        File f2 = tmp.newFile();

        when(switcher.isEnabled(anyString())).thenReturn(true);

        // when
        IOUtil.writeText("   ", f1);
        IOUtil.writeText("\n", f2);

        // then
        assertEquals(null, finder.findPluginClass(asList(f1.toURI().toURL(), f2.toURI().toURL())));
    }

    @Test
    public void problems_loading_impl() throws Exception {
        String fileName = "xxx";
        File f = tmp.newFile(fileName);

        // when
        IOUtil.writeText("Bar", f);

        when(switcher.isEnabled(anyString())).thenThrow(new RuntimeException("Boo!"));

        try {
            // when
            finder.findPluginClass(asList(f.toURI().toURL()));
            // then
            fail();
        } catch (Exception e) {
            assertThat(e).hasMessageContaining(fileName);
            assertThat(e.getCause()).hasMessage("Boo!");
        }
    }

    @Test
    public void multiple_implementations_all_discovered() throws Exception {
        File f1 = tmp.newFile();
        File f2 = tmp.newFile();
        when(switcher.isEnabled(anyString())).thenReturn(true);

        // when
        IOUtil.writeText("foo.Foo", f1);
        IOUtil.writeText("bar.Bar\nbaz.Baz", f2);

        // then
        List<String> foundClasses = finder.findPluginClasses(asList(f1.toURI().toURL(), f2.toURI().toURL()));
        assertThat(foundClasses)
                .as("Returned in the order they were seen")
                .containsExactly(
                        "foo.Foo",
                        "bar.Bar",
                        "baz.Baz");
    }

    @Test
    public void multiple_implementations_duplicates_are_removed() throws Exception {
        File f1 = tmp.newFile();
        File f2 = tmp.newFile();
        when(switcher.isEnabled(anyString())).thenReturn(true);

        // when
        IOUtil.writeText("foo.Foo\nbaz.Baz", f1);
        IOUtil.writeText("bar.Bar\nfoo.Foo\nbaz.Baz", f2);

        // then
        List<String> foundClasses = finder.findPluginClasses(asList(f1.toURI().toURL(), f2.toURI().toURL()));
        assertThat(foundClasses)
                .as("Returned in the order they were seen, with no duplicates")
                .containsExactly(
                        "foo.Foo",
                        "baz.Baz",
                        "bar.Bar");
    }

    @Test
    public void multiple_implementations_some_not_enabled() throws Exception {
        File f1 = tmp.newFile();
        File f2 = tmp.newFile();
        when(switcher.isEnabled(anyString())).thenReturn(true);
        when(switcher.isEnabled(eq("foo.Foo"))).thenReturn(false);

        // when
        IOUtil.writeText("foo.Foo\nbaz.Baz", f1);
        IOUtil.writeText("bar.Bar\nfoo.Foo\nbaz.Baz", f2);

        // then
        List<String> foundClasses = finder.findPluginClasses(asList(f1.toURI().toURL(), f2.toURI().toURL()));
        assertThat(foundClasses)
                .as("Returned in the order they were seen, with no duplicates, excluding disabled classes")
                .containsExactly(
                        "baz.Baz",
                        "bar.Bar");
    }
}
