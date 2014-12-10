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
    public void readerToLinesEmptyString() throws IOException {
        assertEquals(Collections.emptyList(), reader.readerToLines(new StringReader("")));
    }

    @Test
    public void readerToLinesNoLineBreaks() throws IOException {
        assertEquals(Arrays.asList("a"), reader.readerToLines(new StringReader("a")));
    }

    @Test
    public void readerToLinesWithLineBreaks() throws IOException {
        assertEquals(Arrays.asList("a", "b", "c"),
                reader.readerToLines(new StringReader("a\nb\nc")));
    }

    @Test
    public void readerToLinesWithEmptyLines() throws IOException {
        assertEquals(Arrays.asList("a", "", "c"),
                reader.readerToLines(new StringReader("a\n\nc")));
    }

    @Test
    public void stripCommentsAndWhitespaceEmptyInput() throws IOException {
        assertEquals("", reader.stripCommentAndWhitespace(""));
    }

    @Test
    public void stripCommentsAndWhitespaceWhitespaceInput() throws IOException {
        assertEquals("", reader.stripCommentAndWhitespace(" "));
    }

    @Test
    public void stripCommentsAndWhitespaceCommentInInput() throws IOException {
        assertEquals("a", reader.stripCommentAndWhitespace("a#b"));
    }

    @Test
    public void stripCommentsAndWhitespaceMultipleHashes() throws IOException {
        assertEquals("a", reader.stripCommentAndWhitespace("a#b#c"));
    }

    @Test
    public void stripCommentsAndWhitespaceWithWhitespaceAndComments() throws IOException {
        assertEquals("a", reader.stripCommentAndWhitespace(" a #b"));
    }
}