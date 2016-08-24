package org.mockito.internal.matchers.text;


import org.junit.Test;

import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.text.ValuePrinter.print;

public class ValuePrinterTest {

    @Test
    public void prints_values() throws Exception {
        assertEquals("null", print(null));
        assertEquals("\"str\"", print("str"));
        assertEquals("\"x\ny\"", print("x\ny"));
        assertEquals("3", print(3));
        assertEquals("3L", print(3L));
        assertEquals("3.14d", print(3.14d));
        assertEquals("3.14f", print(3.14f));
        assertEquals("[1, 2]", print(new int[]{1, 2}));
        assertEquals("{\"foo\" = 2L}", print(new LinkedHashMap<String, Object>() {
            {
                put("foo", 2L);
            }
        }));
        assertEquals("{\"byte\" = 0x01, \"short\" = 2, \"int\" = 3, \"long\" = 4L, \"float\" = 2.71f, \"double\" = 3.14d}", print(new LinkedHashMap<String, Object>() {
            {
                put("byte", (byte)1);
                put("short", (short)2);
                put("int", 3);
                put("long", 4L);
                put("float", 2.71f);
                put("double", 3.14d);
            }
        }));
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