/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.invocation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class InvocationOnMockTest {

    @Test
    public void ensure_that_count_is_one_after_first_call() {
        Map<String, Integer> mock = mock();
        when(mock.get("foo")).thenAnswer(InvocationOnMock::getInvocationCount);

        assertThat(mock.get("foo")).isEqualTo(1);
    }

    @Test
    public void ensure_that_count_increments_across_calls() {
        Map<String, Integer> mock = mock();
        when(mock.get("foo")).thenAnswer(InvocationOnMock::getInvocationCount);

        assertThat(mock.get("foo")).isEqualTo(1);
        assertThat(mock.get("foo")).isEqualTo(2);
        assertThat(mock.get("foo")).isEqualTo(3);
    }

    @Test
    public void ensure_that_first_n_calls_can_fail_then_return_value() {
        Map<String, Integer> mock = mock();
        when(mock.get("foo"))
                .thenAnswer(
                        inv -> {
                            if (inv.getInvocationCount() <= 2) {
                                throw new RuntimeException("Oupps");
                            }
                            return 200;
                        });

        assertThatThrownBy(() -> mock.get("foo")).hasMessage("Oupps");
        assertThatThrownBy(() -> mock.get("foo")).hasMessage("Oupps");
        assertThat(mock.get("foo")).isEqualTo(200);
    }

    @Test
    public void ensure_that_counts_are_isolated_per_arguments() {
        Map<String, Integer> mock = mock();
        when(mock.get("foo")).thenAnswer(InvocationOnMock::getInvocationCount);
        when(mock.get("bar")).thenAnswer(InvocationOnMock::getInvocationCount);

        mock.get("foo");
        mock.get("foo");
        mock.get("bar");

        assertThat(mock.get("foo")).isEqualTo(3);
        assertThat(mock.get("bar")).isEqualTo(2);
    }

    @Test
    public void ensure_that_counts_are_isolated_per_method() {
        Map<String, Integer> mock = mock();
        when(mock.get("foo")).thenAnswer(InvocationOnMock::getInvocationCount);
        when(mock.put("bar", 2)).thenAnswer(InvocationOnMock::getInvocationCount);

        mock.get("foo");
        mock.put("bar", 2);
        mock.put("bar", 2);
        mock.put("bar", 2);

        assertThat(mock.get("foo")).isEqualTo(2);
        assertThat(mock.put("bar", 2)).isEqualTo(4);
    }

    @Test
    public void ensure_that_counts_are_isolated_per_mock() {
        Map<String, Integer> firstMock = mock();
        Map<String, Integer> secondMock = mock();
        when(firstMock.get("foo")).thenAnswer(InvocationOnMock::getInvocationCount);
        when(secondMock.get("foo")).thenAnswer(InvocationOnMock::getInvocationCount);

        firstMock.get("foo");
        firstMock.get("foo");

        assertThat(firstMock.get("foo")).isEqualTo(3);
        assertThat(secondMock.get("foo")).isEqualTo(1);
    }

    @Test
    public void ensure_that_null_arguments_are_supported() {
        Map<String, Integer> mock = mock();
        when(mock.get(null)).thenAnswer(InvocationOnMock::getInvocationCount);

        assertThat(mock.get(null)).isEqualTo(1);
        assertThat(mock.get(null)).isEqualTo(2);
    }

    @Test
    public void ensure_that_varargs_methods_are_supported() {
        List<String> mock = mock();
        when(mock.toArray(new String[0]))
                .thenAnswer(inv -> new String[] {String.valueOf(inv.getInvocationCount())});

        assertThat(mock.toArray(new String[0])).containsExactly("1");
        assertThat(mock.toArray(new String[0])).containsExactly("2");
    }
}
