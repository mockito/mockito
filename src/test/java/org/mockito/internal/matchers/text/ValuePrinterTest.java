package org.mockito.internal.matchers.text;


import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.text.ValuePrinter.print;

public class ValuePrinterTest {

    @Test
    public void prints_values() throws Exception {
        assertEquals("null", print(null));
        assertEquals("\"str\"", print("str"));
        assertEquals("\"x\ny\"", print("x\ny"));
        assertEquals("[1, 2]", print(new int[]{1, 2}));
        assertTrue(print(new UnsafeToString()).contains("UnsafeToString"));
        assertEquals("ToString", print(new ToString()));
        assertEquals("formatted", print(new FormattedText("formatted")));
    }

    static class ToString {
        public String toString() {
            return "ToString";
        }
    }

    static class UnsafeToString {
        public String toString() {
            throw new RuntimeException("ka-boom!");
        }
    }

    @Test
    public void prints_chars() throws Exception {
        assertEquals("'a'", print('a'));
        assertEquals("'\\n'", print('\n'));
        assertEquals("'\\t'", print('\t'));
        assertEquals("'\\r'", print('\r'));
    }
}