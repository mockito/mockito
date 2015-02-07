package org.mockito.internal.util;

import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class PrettyPrinterTest {

    List<SelfDescribing> items = Arrays.asList(
            new SelfDescribing() {
                public void describeTo(Description description) {
                    description.appendText("1");
                }
            },
            new SelfDescribing() {
                public void describeTo(Description description) {
                    description.appendText("2");
                }
            },
            new SelfDescribing() {
                public void describeTo(Description description) {
                    description.appendText("3");
                }
            }
    );

    @Test
    public void stringify_things_in_line() {
        String expected = "(1, 2, 3);";
        assertEquals(expected, PrettyPrinter.toStringLine(items));
    }

    @Test
    public void empty_line() {
        String expected = "();";
        assertEquals(expected, PrettyPrinter.toStringLine(Collections.EMPTY_LIST));
    }

    @Test
    public void stringify_things_in_block() {
        String expected = "(\n" +
                "    1,\n" +
                "    2,\n" +
                "    3\n" +
                ");";
        assertEquals(expected, PrettyPrinter.toStringBlock(items));
    }

    @Test
    public void empty_block() {
        String expected = "(\n" +
                "    \n" +
                ");";
        assertEquals(expected, PrettyPrinter.toStringBlock(Collections.EMPTY_LIST));
    }


}
