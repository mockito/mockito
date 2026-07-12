/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers.text;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.text.ValuePrinter.print;
import static org.mockito.internal.matchers.text.ValuePrinter.printValues;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
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
    public void prints_fields_reflectively_when_toString_not_overridden() {
        assertThat(print(new NoToString("john", 42))).isEqualTo("NoToString[age=42, name=john]");
        assertThat(print(new ChildWithoutToString("john", 42, new BigDecimal("100.99"))))
                .isEqualTo("ChildWithoutToString[salary=100.99, age=42, name=john]");
    }

    @Test
    public void callsToString_when_toString_is_overridden() {
        assertThat(print(new Credentials("john", "secret")))
                .isEqualTo("Credentials{username:joh.., password:***}");
    }

    @Test
    public void truncates_long_field_values_when_printing_reflectively() {
        String longValue = "x".repeat(200);

        assertThat(print(new NoToString(longValue, 0)))
                .isEqualTo("NoToString[age=0, name=" + "x".repeat(100) + "...]");
    }

    @Test
    public void prints_chars() {
        assertThat(print('a')).isEqualTo("'a'");
        assertThat(print('\n')).isEqualTo("'\\n'");
        assertThat(print('\t')).isEqualTo("'\\t'");
        assertThat(print('\r')).isEqualTo("'\\r'");
        assertThat(print('"')).isEqualTo("'\\\"'");
    }

    @Test
    public void printValues_withDefaultSeparator() {
        List<Integer> values = asList(111, 222, 333);

        assertThat(printValues(null, null, null, values.iterator())).isEqualTo("(111,222,333)");
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

    static class NoToString {
        private static final int MIN_AGE = 23;
        private final String name;
        private final int age;

        NoToString(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    static class ChildWithoutToString extends NoToString {
        private final BigDecimal salary;

        public ChildWithoutToString(String name, int age, BigDecimal salary) {
            super(name, age);
            this.salary = salary;
        }
    }

    static class Credentials {
        private final String username;
        private final String password;

        public Credentials(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        public String toString() {
            return String.format(
                    "Credentials{username:%s.., password:***}", username.substring(0, 3));
        }
    }
}
