/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Captor;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JunitJupiterTest {

    @Mock private Function<Integer, String> rootMock;

    @Captor private ArgumentCaptor<String> rootCaptor;

    @InjectMocks private ClassWithDependency classWithDependency;

    @Test
    void ensure_mock_creation_works() {
        assertThat(rootMock).isNotNull();
    }

    @Test
    void can_set_stubs_on_initialized_mock() {
        Mockito.when(rootMock.apply(10)).thenReturn("Return");
        assertThat(rootMock.apply(10)).isEqualTo("Return");
    }

    @Test
    void ensure_captor_creation_works() {
        assertThat(rootCaptor).isNotNull();
    }

    @Test
    void can_capture_with_initialized_captor() {
        assertCaptor(rootCaptor);
    }

    @Test
    void initializes_parameters(
            @Mock Function<String, String> localMock, @Captor ArgumentCaptor<String> localCaptor) {
        Mockito.when(localMock.apply("Para")).thenReturn("Meter");
        assertThat(localMock.apply("Para")).isEqualTo("Meter");

        assertCaptor(localCaptor);
    }

    @Test
    void initializes_parameters_with_custom_configuration(
            @Mock(name = "overriddenName") Function<String, String> localMock) {
        assertThat(MockUtil.getMockName(localMock).toString()).isEqualTo("overriddenName");
    }

    @Nested
    class NestedTestWithConstructorParameter {
        private final Function<Integer, String> constructorMock;
        private final ArgumentCaptor<String> constructorCaptor;

        NestedTestWithConstructorParameter(
                @Mock Function<Integer, String> constructorMock,
                @Captor ArgumentCaptor<String> constructorCaptor) {
            this.constructorMock = constructorMock;
            this.constructorCaptor = constructorCaptor;
        }

        @Test
        void can_inject_into_constructor_parameter() {
            Mockito.when(constructorMock.apply(42)).thenReturn("42");
            assertThat(constructorMock.apply(42)).isEqualTo("42");

            assertCaptor(constructorCaptor);
        }
    }

    @Nested
    class NestedTestWithExtraMock {
        @Mock Runnable nestedMock;

        @Test
        void nested_mock_created() {
            assertThat(nestedMock).isNotNull();
        }

        @Test
        void root_mock_created() {
            assertThat(rootMock).isNotNull();
        }
    }

    @Nested
    class NestedClassWithExtraCaptor {
        @Captor ArgumentCaptor<Integer> nestedCaptor;

        @Test
        void nested_captor_created() {
            assertThat(nestedCaptor).isNotNull();
        }

        @Test
        void root_captor_created() {
            assertThat(rootCaptor).isNotNull();
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    // ^^ duplicate registration should be ignored by JUnit
    // see https://junit.org/junit5/docs/current/user-guide/#extensions-registration-inheritance
    class DuplicateExtensionOnNestedTest {

        @Mock Object nestedMock;

        @Mock ArgumentCaptor<String> nestedCaptor;

        @Test
        void ensure_mocks_are_created_for_nested_tests() {
            assertThat(nestedMock).isNotNull();
        }

        @Test
        void ensure_captor_is_created_for_nested_tests() {
            assertThat(nestedCaptor).isNotNull();
        }
    }

    @Nested
    class NestedWithoutExtraMock {
        @Test
        // mock is initialized by mockito session
        void shouldWeCreateMocksInTheParentContext() {
            assertThat(rootMock).isNotNull();
        }
    }

    @Test
    void should_be_injected_correct_instance_of_mock() {
        assertThat(classWithDependency.dependency).isSameAs(rootMock);
    }

    private static class ClassWithDependency {
        private final Function<Integer, String> dependency;

        private ClassWithDependency(Function<Integer, String> dependency) {
            this.dependency = dependency;
        }
    }

    @SuppressWarnings("unchecked")
    private static void assertCaptor(ArgumentCaptor<String> captor) {
        Predicate<String> mock = Mockito.mock(Predicate.class);
        ProductionCode.simpleMethod(mock, "parameter");
        verify(mock).test(captor.capture());
        assertThat(captor.getValue()).isEqualTo("parameter");
    }
}
