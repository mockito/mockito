/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.junit.Test;
import org.mockito.internal.matchers.text.MatchersPrinter;
import org.mockito.internal.reporting.PrintSettings;
import org.mockitoutil.TestBase;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

@SuppressWarnings("unchecked")
public class MatchersPrinterTest extends TestBase {

    private final MatchersPrinter printer = new MatchersPrinter();

    @Test
    public void shouldGetArgumentsLine() {
        String line = printer.getArgumentsLine((List) Arrays.asList(new Equals(1), new Equals(2)), new PrintSettings());
        assertEquals("(1, 2);", line);
    }

    @Test
    public void shouldGetArgumentsBlock() {
        String line = printer.getArgumentsBlock((List) Arrays.asList(new Equals(1), new Equals(2)), new PrintSettings());
        assertEquals("(\n    1,\n    2\n);", line);
    }

    @Test
    public void shouldDescribeTypeInfoOnlyMarkedMatchers() {
        //when
        String line = printer.getArgumentsLine((List) Arrays.asList(new Equals(1L), new Equals(2)), PrintSettings.verboseMatchers(1));
        //then
        assertEquals("(1, (Integer) 2);", line);
    }

    @Test
    public void shouldDescribeStringMatcher() {
        //when
        String line = printer.getArgumentsLine((List) Arrays.asList(new Equals(1L), new Equals("x")), PrintSettings.verboseMatchers(1));
        //then
        assertEquals("(1, (String) \"x\");", line);
    }

    @Test
    public void shouldGetVerboseArgumentsInBlock() {
        //when
        String line = printer.getArgumentsBlock((List) Arrays.asList(new Equals(1L), new Equals(2)), PrintSettings.verboseMatchers(0, 1));
        //then
        assertEquals("(\n    (Long) 1,\n    (Integer) 2\n);", line);
    }

    @Test
    public void shouldGetVerboseArgumentsEvenIfSomeMatchersAreNotVerbose() {
        //when
        String line = printer.getArgumentsLine((List) Arrays.asList(new Equals(1L), NotNull.NOT_NULL), PrintSettings.verboseMatchers(0));
        //then
        assertEquals("((Long) 1, notNull());", line);
    }
}