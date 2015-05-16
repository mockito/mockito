/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;
import org.junit.Test;
import org.mockito.internal.reporting.PrintSettings;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class MatchersPrinterTest extends TestBase {

    MatchersPrinter printer = new MatchersPrinter();

    private String toString(List<SelfDescribing> items) {
        return new StringDescription().appendList("(", ", ", ");", items).toString();
    }

    @Test
    public void should_get_matchers() {
        List<SelfDescribing> matchers = printer.describe((List) Arrays.asList(new Equals(1), new Equals(2)), new PrintSettings());
        assertEquals("(1, 2);", toString(matchers));
    }

    @Test
    public void should_describe_type_info_only_for_marked_matchers() {
        //when
        List<SelfDescribing> matchers = printer.describe((List) Arrays.asList(new Equals(1L), new Equals(2)), PrintSettings.verboseMatchers(1));
        //then
        assertEquals("(1, (Integer) 2);", toString(matchers));
    }

    @Test
    public void should_get_verbose_matchers() {
        //when
        List<SelfDescribing> matchers = printer.describe((List) Arrays.asList(new Equals(1L), new Equals(2)), PrintSettings.verboseMatchers(0, 1));
        //then
        assertEquals("((Long) 1, (Integer) 2);", toString(matchers));
    }

    @Test
    public void should_get_verbose_matchers_even_if_some_matchers_are_not_verbose() {
        //when
        List<SelfDescribing> matchers = printer.describe((List) Arrays.asList(new Equals(1L), NotNull.NOT_NULL), PrintSettings.verboseMatchers(0));
        //then
        assertEquals("((Long) 1, notNull());", toString(matchers));
    }
}