/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers.apachecommons;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mockitoutil.TestBase;

public class ReflectionEqualsTest extends TestBase {

    static class NoToString {
        private final String name;
        private final int age;

        NoToString(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    static class WithToString {
        private final String name;

        WithToString(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "WithToString(" + name + ")";
        }
    }

    static class LongField {
        private final String value;

        LongField(String value) {
            this.value = value;
        }
    }

    @Test
    public void prints_fields_reflectively_when_toString_not_overridden() {
        ReflectionEquals matcher = new ReflectionEquals(new NoToString("john", 42));

        assertThat(matcher.toString()).isEqualTo("refEq(NoToString[age=42, name=john])");
    }

    @Test
    public void uses_declared_toString_when_present() {
        ReflectionEquals matcher = new ReflectionEquals(new WithToString("john"));

        assertThat(matcher.toString()).isEqualTo("refEq(WithToString(john))");
    }

    @Test
    public void prints_null_as_null() {
        ReflectionEquals matcher = new ReflectionEquals(null);

        assertThat(matcher.toString()).isEqualTo("refEq(null)");
    }

    @Test
    public void truncates_long_field_values() {
        String longValue = "x".repeat(200);
        ReflectionEquals matcher = new ReflectionEquals(new LongField(longValue));

        assertThat(matcher.toString())
                .isEqualTo("refEq(LongField[value=" + "x".repeat(100) + "...])");
    }
}
