package org.mockito.internal.configuration.plugins;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.io.IOUtil;
import org.mockito.plugins.PluginSwitcher;
import org.mockitoutil.TestBase;

import java.io.File;

import static java.util.Arrays.asList;

public class PluginFinderTest extends TestBase {

    @Mock PluginSwitcher switcher;
    @InjectMocks PluginFinder finder;
    public @Rule TemporaryFolder tmp = new TemporaryFolder();

    @Test public void empty_resources() {
        assertNull(finder.findPluginClass((Iterable) asList()));
    }

    @Test public void no_valid_impl() throws Exception {
        File f = tmp.newFile();

        //when
        IOUtil.writeText("  \n  ", f);

        //then
        assertNull(finder.findPluginClass((Iterable) asList(f.toURI().toURL())));
    }

    @Test public void single_implementation() throws Exception {
        File f = tmp.newFile();

        //when
        IOUtil.writeText("  foo.Foo  ", f);

        //then
        assertEquals("foo.Foo", finder.findPluginClass((Iterable) asList(f.toURI().toURL())));
    }

    @Test public void multiple_implementations() {
        fail();
    }

    @Test public void multiple_empty_implementations() {
        fail();
    }

    @Test public void single_usable_impl() {
        fail();
    }

    @Test public void problems_loading_impl() {
        fail();
    }
}