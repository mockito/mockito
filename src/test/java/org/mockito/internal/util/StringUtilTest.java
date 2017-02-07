/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.util;

import static junit.framework.TestCase.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class StringUtilTest  {

    @Test
    public void decamelizeMatcher() throws Exception {
        assertEquals("<Sentence with strong language>", StringUtil.decamelizeMatcher("SentenceWithStrongLanguage"));
        assertEquals("<W e i r d o 1>", StringUtil.decamelizeMatcher("WEIRDO1"));
        assertEquals("<_>", StringUtil.decamelizeMatcher("_"));
        assertEquals("<Has exactly 3 elements>", StringUtil.decamelizeMatcher("HasExactly3Elements"));
        assertEquals("<custom argument matcher>", StringUtil.decamelizeMatcher(""));
    }

    @Test
    public void join_non() throws Exception {
        assertThat(StringUtil.join()).isEmpty();;
    }

    @Test
    public void join_singleLine() throws Exception {
        assertThat(StringUtil.join("line1")).hasLineCount(2);
    }

    @Test
    public void join_twoLines() throws Exception {
        assertThat(StringUtil.join("line1","line2")).hasLineCount(3);
    }

    @Test
    public void join_hasPreceedingLinebreak() throws Exception {
        assertThat(StringUtil.join("line1")).isEqualTo("\nline1");
    }

    @Test
    public void removeFirstLine() throws Exception {
        assertThat(StringUtil.removeFirstLine("line1\nline2")).isEqualTo("line2");
    }
}
