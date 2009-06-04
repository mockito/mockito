package org.mockito.internal.matchers;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.invocation.PrintSettings;
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
    public void shouldGetVerboseArguments() {
        String line = printer.getArgumentsLine((List) Arrays.asList(new Equals(1L), new Equals(2)), PrintSettings.verboseArgs());
        assertEquals("((Long) 1, (Integer) 2);", line);
    }

    @Test
    public void shouldGetVerboseArgumentsInBlock() {
        String line = printer.getArgumentsBlock((List) Arrays.asList(new Equals(1L), new Equals(2)), PrintSettings.verboseArgs());
        assertEquals("(\n    (Long) 1,\n    (Integer) 2\n);", line);
    }

    @Test
    public void shouldGetVerboseArgumentsEvenIfSomeMatchersAreNotVerbose() {
        String line = printer.getArgumentsLine((List) Arrays.asList(new Equals(1L), NotNull.NOT_NULL), PrintSettings.verboseArgs());
        assertEquals("((Long) 1, notNull());", line);
    }
}