/*
 * Copyright (c) 2023 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;

/*
 * Sealed classes were
 * * introduced in Java 15 as a preview JEP-360 https://openjdk.org/jeps/360
 * * followed by a second preview in Java 16 JEP-397 https://openjdk.org/jeps/397
 * * finalized in Java 17 JEP-409 https://openjdk.org/jeps/409
 *
 *
 */
@SuppressWarnings("NewClassNamingConvention")
public class DeepStubReturnsEnum_Java17_issue_2984_Test {

    @Test
    public void cant_mock_enum_class() {
        assertThatThrownBy(
                        () -> {
                            mock(TestEnum.class);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining(
                        "Sealed abstract enums can't be mocked. Since Java 15 abstract enums are declared sealed, which prevents mocking.");
    }

    @Test
    public void cant_mock_enum_class_as_deep_stub() {
        assertThatThrownBy(
                        () -> {
                            mock(TestEnum.class, RETURNS_DEEP_STUBS);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining(
                        "Sealed abstract enums can't be mocked. Since Java 15 abstract enums are declared sealed, which prevents mocking.");
    }

    @Test
    public void deep_stub_cant_mock_enum_with_abstract_method() {
        final var mock = mock(TestClass.class, RETURNS_DEEP_STUBS);
        assertThatThrownBy(
                        () -> {
                            mock.getTestEnum();
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining(
                        "Sealed abstract enums can't be mocked. Since Java 15 abstract enums are declared sealed, which prevents mocking.");
    }

    @Test
    public void deep_stub_can_override_mock_enum_with_abstract_method() {
        final var mock = mock(TestClass.class, RETURNS_DEEP_STUBS);
        // We need the doReturn() because when calling when(mock.getTestEnum()) it will already
        // throw an exception.
        doReturn(TestEnum.A).when(mock).getTestEnum();

        assertThat(mock.getTestEnum()).isEqualTo(TestEnum.A);
        assertThat(mock.getTestEnum().getValue()).isEqualTo("A");

        assertThat(mockingDetails(mock.getTestEnum()).isMock()).isFalse();
    }

    @Test
    public void deep_stub_can_mock_enum_without_method() {
        final var mock = mock(TestClass.class, RETURNS_DEEP_STUBS);
        assertThat(mock.getTestNonAbstractEnum()).isNotNull();

        assertThat(mockingDetails(mock.getTestNonAbstractEnum()).isMock()).isTrue();
        when(mock.getTestNonAbstractEnum()).thenReturn(TestNonAbstractEnum.B);
        assertThat(mock.getTestNonAbstractEnum()).isEqualTo(TestNonAbstractEnum.B);
    }

    @Test
    public void deep_stub_can_mock_enum_without_abstract_method() {
        final var mock = mock(TestClass.class, RETURNS_DEEP_STUBS);
        assertThat(mock.getTestNonAbstractEnumWithMethod()).isNotNull();
        assertThat(mock.getTestNonAbstractEnumWithMethod().getValue()).isNull();
        assertThat(mockingDetails(mock.getTestNonAbstractEnumWithMethod()).isMock()).isTrue();

        when(mock.getTestNonAbstractEnumWithMethod().getValue()).thenReturn("Mock");
        assertThat(mock.getTestNonAbstractEnumWithMethod().getValue()).isEqualTo("Mock");

        when(mock.getTestNonAbstractEnumWithMethod()).thenReturn(TestNonAbstractEnumWithMethod.B);
        assertThat(mock.getTestNonAbstractEnumWithMethod())
                .isEqualTo(TestNonAbstractEnumWithMethod.B);
    }

    @Test
    public void mock_mocking_enum_getter() {
        final var mock = mock(TestClass.class);
        when(mock.getTestEnum()).thenReturn(TestEnum.B);
        assertThat(mock.getTestEnum()).isEqualTo(TestEnum.B);
        assertThat(mock.getTestEnum().getValue()).isEqualTo("B");
    }

    static class TestClass {
        TestEnum getTestEnum() {
            return TestEnum.A;
        }

        TestNonAbstractEnumWithMethod getTestNonAbstractEnumWithMethod() {
            return TestNonAbstractEnumWithMethod.A;
        }

        TestNonAbstractEnum getTestNonAbstractEnum() {
            return TestNonAbstractEnum.A;
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

    enum TestNonAbstractEnum {
        A,
        B
    }

    enum TestNonAbstractEnumWithMethod {
        A,
        B;

        String getValue() {
            return "RealValue";
        }
    }
}
