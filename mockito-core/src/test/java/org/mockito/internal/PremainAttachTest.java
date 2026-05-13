/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import org.junit.Test;

import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

public class PremainAttachTest {

    @Test
    public void parse_suppress_clinit_property_with_multiple_classes() {
        Predicate<String> matcher =
                PremainAttach.buildClinitPredicate(
                        "com.example.ClassA,com.example.ClassB,com.example.ClassC");

        assertThat(matcher).isNotNull();
        assertThat(matcher.test("com.example.ClassA")).isTrue();
        assertThat(matcher.test("com.example.ClassB")).isTrue();
        assertThat(matcher.test("com.example.ClassC")).isTrue();
        assertThat(matcher.test("com.example.Other")).isFalse();
    }

    @Test
    public void parse_suppress_clinit_property_with_whitespace_trimming() {
        Predicate<String> matcher =
                PremainAttach.buildClinitPredicate(" com.example.ClassA , com.example.ClassB ");

        assertThat(matcher).isNotNull();
        assertThat(matcher.test("com.example.ClassA")).isTrue();
        assertThat(matcher.test("com.example.ClassB")).isTrue();
    }

    @Test
    public void parse_suppress_clinit_property_with_one_empty_class() {
        Predicate<String> matcher =
                PremainAttach.buildClinitPredicate(" com.example.ClassA , , com.example.ClassB ");

        assertThat(matcher).isNotNull();
        assertThat(matcher.test("com.example.ClassA")).isTrue();
        assertThat(matcher.test("com.example.ClassB")).isTrue();
    }

    @Test
    public void parse_suppress_clinit_property_returns_null_when_null() {
        assertThat(PremainAttach.buildClinitPredicate(null)).isNull();
    }

    @Test
    public void parse_suppress_clinit_property_returns_null_when_blank() {
        assertThat(PremainAttach.buildClinitPredicate("   ")).isNull();
    }

    @Test
    public void parse_suppress_clinit_property_returns_null_when_only_empty_entries() {
        assertThat(PremainAttach.buildClinitPredicate(" , , ")).isNull();
    }

    @Test
    public void parse_suppress_clinit_property_with_package_wildcard_matches_recursively() {
        Predicate<String> matcher = PremainAttach.buildClinitPredicate("com.example.*");

        assertThat(matcher).isNotNull();
        assertThat(matcher.test("com.example.Foo")).isTrue();
        assertThat(matcher.test("com.example.bar.Baz")).isTrue();
        assertThat(matcher.test("com.example.a.b.c.Deep")).isTrue();
    }

    @Test
    public void parse_suppress_clinit_property_with_package_wildcard_rejects_sibling_packages() {
        Predicate<String> matcher = PremainAttach.buildClinitPredicate("com.example.*");

        assertThat(matcher).isNotNull();
        assertThat(matcher.test("com.examples.Foo")).isFalse();
        assertThat(matcher.test("com.exampleX.Foo")).isFalse();
        assertThat(matcher.test("org.example.Foo")).isFalse();
    }

    @Test
    public void parse_suppress_clinit_property_mixes_classes_and_packages() {
        Predicate<String> matcher =
                PremainAttach.buildClinitPredicate(
                        "com.example.ClassA, com.pkg.*, com.example.ClassB");

        assertThat(matcher).isNotNull();
        assertThat(matcher.test("com.example.ClassA")).isTrue();
        assertThat(matcher.test("com.example.ClassB")).isTrue();
        assertThat(matcher.test("com.pkg.Anything")).isTrue();
        assertThat(matcher.test("com.pkg.nested.Thing")).isTrue();
        assertThat(matcher.test("com.example.ClassC")).isFalse();
        assertThat(matcher.test("com.pkgs.Other")).isFalse();
    }

    @Test
    public void parse_suppress_clinit_property_with_middle_wildcard_is_not_supported() {
        Predicate<String> matcher = PremainAttach.buildClinitPredicate("com.*.bar.*");

        assertThat(matcher).isNotNull();
        assertThat(matcher.test("com.foo.bar.Foo")).isFalse();
        assertThat(matcher.test("com.any.bar.Bar")).isFalse();
    }
}
