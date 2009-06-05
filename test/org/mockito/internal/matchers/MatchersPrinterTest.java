package org.mockito.internal.matchers;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.reporting.PrintSettings;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class MatchersPrinterTest extends TestBase {

    MatchersPrinter printer = new MatchersPrinter();

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
    public void shouldPrintVerboselyOnlyMarkedMatchers() {
        //given
        Equals verboselyPrinted = new Equals(2);
        //when
        String line = printer.getArgumentsLine((List) Arrays.asList(new Equals(1L), verboselyPrinted), PrintSettings.verboseMatchers(verboselyPrinted));
        //then
        assertEquals("(1, (Integer) 2);", line);
    }

    @Test
    public void shouldGetVerboseArgumentsInBlock() {
        //given
        Equals verboselyPrinted = new Equals(1L);
        Equals verboselyPrintedTwo = new Equals(2);
        //when
        String line = printer.getArgumentsBlock((List) Arrays.asList(verboselyPrinted, verboselyPrintedTwo), PrintSettings.verboseMatchers(verboselyPrinted, verboselyPrintedTwo));
        //then
        assertEquals("(\n    (Long) 1,\n    (Integer) 2\n);", line);
    }

    @Test
    public void shouldGetVerboseArgumentsEvenIfSomeMatchersAreNotVerbose() {
        //given
        Equals verboselyPrinted = new Equals(1L);
        //when
        String line = printer.getArgumentsLine((List) Arrays.asList(verboselyPrinted, NotNull.NOT_NULL), PrintSettings.verboseMatchers(verboselyPrinted));
        //then
        assertEquals("((Long) 1, notNull());", line);
    }
}