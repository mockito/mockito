/*
 * Copyright (c) 2023 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class DeepStubReturnsEnumJava11Test {
    private static final String MOCK_VALUE = "Mock";

    @Test
    public void deep_stub_can_mock_enum_getter_Issue_2984() {
        final var mock = mock(TestClass.class, RETURNS_DEEP_STUBS);
        when(mock.getTestEnum()).thenReturn(TestEnum.B);
        assertThat(mock.getTestEnum()).isEqualTo(TestEnum.B);
    }

    @Test
    public void deep_stub_can_mock_enum_class_Issue_2984() {
        final var mock = mock(TestEnum.class, RETURNS_DEEP_STUBS);
        when(mock.getValue()).thenReturn(MOCK_VALUE);
        assertThat(mock.getValue()).isEqualTo(MOCK_VALUE);
    }

    @Test
    public void deep_stub_can_mock_enum_method_Issue_2984() {
        final var mock = mock(TestClass.class, RETURNS_DEEP_STUBS);
        assertThat(mock.getTestEnum().getValue()).isEqualTo(null);

        when(mock.getTestEnum().getValue()).thenReturn(MOCK_VALUE);
        assertThat(mock.getTestEnum().getValue()).isEqualTo(MOCK_VALUE);
    }

    @Test
    public void mock_mocking_enum_getter_Issue_2984() {
        final var mock = mock(TestClass.class);
        when(mock.getTestEnum()).thenReturn(TestEnum.B);
        assertThat(mock.getTestEnum()).isEqualTo(TestEnum.B);
        assertThat(mock.getTestEnum().getValue()).isEqualTo("B");
    }

    static class TestClass {
        TestEnum getTestEnum() {
            return TestEnum.A;
        }
    }

    enum TestEnum {
        A {
            @Override
            String getValue() {
                return this.name();
            }
        },
        B {
            @Override
            String getValue() {
                return this.name();
            }
        },
        ;

        abstract String getValue();
    }
}
