/*
 * Copyright (c) 2025 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

/**
 * Tests for {@link Sets} utility class which provides factory methods
 * for creating mock-safe hash sets and regular sets.
 */
public class SetsTest {

    interface TestInterface {
        void doSomething();
    }

    @Test
    public void should_create_mock_safe_hash_set_from_iterable() {
        List<Object> mocks = new ArrayList<>();
        TestInterface mock1 = mock(TestInterface.class);
        TestInterface mock2 = mock(TestInterface.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Set<Object> result = Sets.newMockSafeHashSet(mocks);

        assertThat(result).hasSize(2);
        assertThat(result).contains(mock1, mock2);
    }

    @Test
    public void should_create_mock_safe_hash_set_from_varargs() {
        TestInterface mock1 = mock(TestInterface.class);
        TestInterface mock2 = mock(TestInterface.class);

        Set<Object> result = Sets.newMockSafeHashSet(mock1, mock2);

        assertThat(result).hasSize(2);
        assertThat(result).contains(mock1, mock2);
    }

    @Test
    public void should_create_empty_mock_safe_hash_set_from_empty_iterable() {
        List<Object> emptyList = new ArrayList<>();

        Set<Object> result = Sets.newMockSafeHashSet(emptyList);

        assertThat(result).isEmpty();
    }

    @Test
    public void should_create_empty_mock_safe_hash_set_from_no_args() {
        Set<Object> result = Sets.newMockSafeHashSet();

        assertThat(result).isEmpty();
    }

    @Test
    public void should_handle_null_in_iterable_for_mock_safe_hash_set() {
        List<Object> mocks = new ArrayList<>();
        mocks.add(null);
        mocks.add(mock(TestInterface.class));

        Set<Object> result = Sets.newMockSafeHashSet(mocks);

        assertThat(result).hasSize(2);
        // Explicit type casting is required for AssertJ's contains() method
        // when checking for null values to avoid NullPointerException
        assertThat(result).contains((Object) null);
    }

    @Test
    public void should_handle_duplicate_mocks_in_mock_safe_hash_set() {
        TestInterface mock = mock(TestInterface.class);

        Set<Object> result = Sets.newMockSafeHashSet(mock, mock);

        // Since mock objects are the same instance, only one is included in the Set
        assertThat(result).hasSize(1);
        assertThat(result).contains(mock);
    }

    @Test
    public void should_create_set_with_string_elements() {
        Set<String> result = Sets.newSet("a", "b", "c");

        assertThat(result).containsExactly("a", "b", "c");
    }

    @Test
    public void should_create_set_with_integer_elements() {
        Set<Integer> result = Sets.newSet(1, 2, 3);

        assertThat(result).containsExactly(1, 2, 3);
    }

    @Test
    public void should_create_empty_set() {
        Set<String> result = Sets.newSet();

        assertThat(result).isEmpty();
    }

    @Test
    public void should_preserve_order_in_new_set() {
        Set<Integer> result = Sets.newSet(3, 1, 2);

        // LinkedHashSet preserves insertion order
        assertThat(result).containsExactly(3, 1, 2);
    }

    @Test
    public void should_handle_duplicate_elements_in_new_set() {
        Set<String> result = Sets.newSet("a", "b", "a", "c");

        // LinkedHashSet removes duplicates but preserves order
        assertThat(result).containsExactly("a", "b", "c");
    }

    @Test
    public void should_throw_exception_when_null_passed_to_new_set() {
        assertThatThrownBy(() -> Sets.newSet((String[]) null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Expected an array of elements");
    }

    @Test
    public void should_handle_null_elements_in_new_set() {
        Set<String> result = Sets.newSet("a", null, "b");

        // Explicit type casting is required for AssertJ's containsExactly() method
        // when checking for null values to avoid NullPointerException
        assertThat(result).containsExactly("a", (String) null, "b");
    }

    @Test
    public void should_create_set_with_mixed_types_using_generics() {
        Set<Object> result = Sets.newSet("string", 123, true);

        assertThat(result).hasSize(3);
        assertThat(result).contains("string", 123, true);
    }

    @Test
    public void should_allow_adding_to_mock_safe_hash_set() {
        TestInterface mock1 = mock(TestInterface.class);
        TestInterface mock2 = mock(TestInterface.class);

        Set<Object> result = Sets.newMockSafeHashSet(mock1);
        result.add(mock2);

        assertThat(result).hasSize(2);
        assertThat(result).contains(mock1, mock2);
    }

    @Test
    public void should_allow_removing_from_mock_safe_hash_set() {
        TestInterface mock1 = mock(TestInterface.class);
        TestInterface mock2 = mock(TestInterface.class);

        Set<Object> result = Sets.newMockSafeHashSet(mock1, mock2);
        result.remove(mock1);

        assertThat(result).hasSize(1);
        assertThat(result).contains(mock2);
    }

    @Test
    public void should_handle_mock_safe_hash_set_with_regular_objects() {
        String regularObject1 = "regular1";
        String regularObject2 = "regular2";

        Set<Object> result = Sets.newMockSafeHashSet(regularObject1, regularObject2);

        assertThat(result).hasSize(2);
        assertThat(result).contains(regularObject1, regularObject2);
    }
}
