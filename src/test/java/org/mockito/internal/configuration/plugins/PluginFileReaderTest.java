/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.plugins;

import org.junit.Test;
import org.mockitoutil.TestBase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PluginFileReaderTest extends TestBase {

    PluginFileReader reader = new PluginFileReader();

    @Test
    public void no_class_in_resource() throws IOException {
        //no class
        assertNull(reader.readPluginClass(impl("")));
        assertNull(reader.readPluginClass(impl("  ")));
        assertNull(reader.readPluginClass(impl(" \n ")));

        //commented out
        assertNull(reader.readPluginClass(impl("#foo")));
        assertNull(reader.readPluginClass(impl("  # foo  ")));
        assertNull(reader.readPluginClass(impl("  # # # java.langString # ")));
        assertNull(reader.readPluginClass(impl("  \n # foo \n # foo \n ")));
    }

    private InputStream impl(String s) {
        return new ByteArrayInputStream(s.getBytes());
    }

    @Test
    public void reads_class_name() throws IOException {
        assertEquals("java.lang.String", reader.readPluginClass(impl("java.lang.String")));
        assertEquals("x", reader.readPluginClass(impl("x")));
        assertEquals("x y z", reader.readPluginClass(impl(" x y z ")));
        assertEquals("foo.Foo", reader.readPluginClass(impl(" #my class\n  foo.Foo \n #other class ")));
        assertEquals("foo.Foo", reader.readPluginClass(impl("foo.Foo  # cool class")));
    }
}
