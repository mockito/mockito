/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PremainAttachTest {

    @Test
    public void parse_suppress_clinit_property_with_multiple_classes() {
        Set<String> result =
                PremainAttach.parseSuppressClinitProperty(
                        "com.example.ClassA,com.example.ClassB,com.example.ClassC");

        assertThat(result)
                .containsExactlyInAnyOrder(
                        "com.example.ClassA", "com.example.ClassB", "com.example.ClassC");
    }

    @Test
    public void parse_suppress_clinit_property_with_whitespace_trimming() {
        Set<String> result =
                PremainAttach.parseSuppressClinitProperty(
                        " com.example.ClassA , com.example.ClassB ");

        assertThat(result).containsExactlyInAnyOrder("com.example.ClassA", "com.example.ClassB");
    }

    @Test
    public void parse_suppress_clinit_property_with_one_empty_class() {
        Set<String> result =
                PremainAttach.parseSuppressClinitProperty(
                        " com.example.ClassA , , com.example.ClassB ");

        assertThat(result).containsExactlyInAnyOrder("com.example.ClassA", "com.example.ClassB");
    }

    @Test
    public void parse_suppress_clinit_property_returns_empty_when_null() {
        Set<String> result = PremainAttach.parseSuppressClinitProperty(null);

        assertThat(result).isEmpty();
    }

    @Test
    public void parse_suppress_clinit_property_returns_empty_when_blank() {
        Set<String> result = PremainAttach.parseSuppressClinitProperty("   ");

        assertThat(result).isEmpty();
    }
}
