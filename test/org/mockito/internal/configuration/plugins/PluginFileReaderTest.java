/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.plugins;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import static org.mockito.Mockito.*;

import junit.framework.Assert;
import org.junit.Test;
import org.mockito.internal.configuration.ClassPathLoader;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class PluginFileReaderTest extends TestBase {

    PluginFileReader reader = new PluginFileReader();

    @Test
    public void no_class_in_resource() throws IOException {
        //no class
        assertNull(reader.readPluginClass(new StringReader("")));
        assertNull(reader.readPluginClass(new StringReader("  ")));
        assertNull(reader.readPluginClass(new StringReader(" \n ")));

        //commented out
        assertNull(reader.readPluginClass(new StringReader("#foo")));
        assertNull(reader.readPluginClass(new StringReader("  # foo  ")));
        assertNull(reader.readPluginClass(new StringReader("  # # # java.langString # ")));
        assertNull(reader.readPluginClass(new StringReader("  \n # foo \n # foo \n ")));
    }

    @Test
    public void reads_class_name() throws IOException {
        assertEquals("java.lang.String", reader.readPluginClass(new StringReader("java.lang.String")));
        assertEquals("x", reader.readPluginClass(new StringReader("x")));
        assertEquals("x y z", reader.readPluginClass(new StringReader(" x y z ")));
        assertEquals("foo.Foo", reader.readPluginClass(new StringReader(" #my class\n  foo.Foo \n #other class ")));
        assertEquals("foo.Foo", reader.readPluginClass(new StringReader("foo.Foo  # cool class")));
    }
}