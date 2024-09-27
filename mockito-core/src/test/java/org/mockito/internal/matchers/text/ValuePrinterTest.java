/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers.text;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.text.ValuePrinter.print;

import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.Test;

public class ValuePrinterTest {

    @Test
    public void prints_values() {
        assertThat(print(null)).isEqualTo("null");
        assertThat(print("str")).isEqualTo("\"str\"");
        assertThat(print("x\ny")).isEqualTo("\"x\ny\"");
        assertThat(print(3)).isEqualTo("3");
        assertThat(print(3L)).isEqualTo("3L");
        assertThat(print(3.14d)).isEqualTo("3.14d");
        assertThat(print(3.14f)).isEqualTo("3.14f");
        assertThat(print(new int[] {1, 2})).isEqualTo("[1, 2]");

        Map<String, Object> map1 = new LinkedHashMap<>();
        map1.put("foo", 2L);
        assertThat(print(map1)).isEqualTo("{\"foo\" = 2L}");

        Map<String, Object> map2 = new LinkedHashMap<>();
        map2.put("int passed as hex", 0x01);
        map2.put("byte", (byte) 0x01);
        map2.put("short", (short) 2);
        map2.put("int", 3);
        map2.put("long", 4L);
        map2.put("float", 2.71f);
        map2.put("double", 3.14d);
        assertThat(print(map2))
                .isEqualTo(
                        "{\"int passed as hex\" = 1, \"byte\" = (byte) 0x01, \"short\" = (short) 2, \"int\" = 3, \"long\" = 4L, \"float\" = 2.71f, \"double\" = 3.14d}");

        assertTrue(print(new UnsafeToString()).contains("UnsafeToString"));
        assertThat(print(new ToString())).isEqualTo("ToString");
        assertThat(print(new FormattedText("formatted"))).isEqualTo("formatted");
    }

    @Test
    public void prints_chars() {
        assertThat(print('a')).isEqualTo("'a'");
        assertThat(print('\n')).isEqualTo("'\\n'");
        assertThat(print('\t')).isEqualTo("'\\t'");
        assertThat(print('\r')).isEqualTo("'\\r'");
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
}
