/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class ReadingConfigurationFromClasspathTest extends TestBase {

    @Test
    public void shouldReadConfigurationClassFromClassPath() {
        ConfigurationAccess.getConfig().overrideDefaultAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                return "foo";
            }});

        IMethods mock = mock(IMethods.class);
        assertEquals("foo", mock.simpleMethod());
    }

    @Test
    public void readerToLinesEmptyString() throws IOException {
        assertEquals(Collections.emptyList(), ClassPathLoader.readerToLines(new StringReader("")));
    }

    @Test
    public void readerToLinesNoLineBreaks() throws IOException {
        assertEquals(Arrays.asList("a"), ClassPathLoader.readerToLines(new StringReader("a")));
    }

    @Test
    public void readerToLinesWithLineBreaks() throws IOException {
        assertEquals(Arrays.asList("a", "b", "c"),
                ClassPathLoader.readerToLines(new StringReader("a\nb\nc")));
    }

    @Test
    public void readerToLinesWithEmptyLines() throws IOException {
        assertEquals(Arrays.asList("a", "", "c"),
                ClassPathLoader.readerToLines(new StringReader("a\n\nc")));
    }

    @Test
    public void stripCommentsAndWhitespaceEmptyInput() throws IOException {
        assertEquals("", ClassPathLoader.stripCommentAndWhitespace(""));
    }

    @Test
    public void stripCommentsAndWhitespaceWhitespaceInput() throws IOException {
        assertEquals("", ClassPathLoader.stripCommentAndWhitespace(" "));
    }

    @Test
    public void stripCommentsAndWhitespaceCommentInInput() throws IOException {
        assertEquals("a", ClassPathLoader.stripCommentAndWhitespace("a#b"));
    }

    @Test
    public void stripCommentsAndWhitespaceMultipleHashes() throws IOException {
        assertEquals("a", ClassPathLoader.stripCommentAndWhitespace("a#b#c"));
    }

    @Test
    public void stripCommentsAndWhitespaceWithWhitespaceAndComments() throws IOException {
        assertEquals("a", ClassPathLoader.stripCommentAndWhitespace(" a #b"));
    }
}