/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import java.util.function.Function;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JunitJupiterTest {

    @Mock
    private Function<Integer, String> rootMock;

    @InjectMocks
    private ClassWithDependency classWithDependency;

    @Test
    void ensureMockCreationWorks() {
        assertThat(rootMock).isNotNull();
    }

    @Test
    void can_set_stubs_on_initialized_mock() {
        Mockito.when(rootMock.apply(10)).thenReturn("Return");
        assertThat(rootMock.apply(10)).isEqualTo("Return");
    }

    @Test
    void initializes_parameters(@Mock Function<String, String> localMock) {
        Mockito.when(localMock.apply("Para")).thenReturn("Meter");
        assertThat(localMock.apply("Para")).isEqualTo("Meter");
    }

    @Test
    void initializes_parameters_with_custom_configuration(@Mock(name = "overriddenName") Function<String, String> localMock) {
        assertThat(MockUtil.getMockName(localMock).toString()).isEqualTo("overriddenName");
    }

    @Nested
    class NestedTestWithConstructorParameter {
        private final Function<Integer, String> constructorMock;

        NestedTestWithConstructorParameter(@Mock Function<Integer, String> constructorMock) {
            this.constructorMock = constructorMock;
        }

        @Test
        void can_inject_into_constructor_parameter() {
            Mockito.when(constructorMock.apply(42)).thenReturn("42");
            assertThat(constructorMock.apply(42)).isEqualTo("42");
        }
    }

    @Nested
    class NestedTestWithExtraMock {
        @Mock Runnable nestedMock;

        @Test
        void nestedMockCreated() {
            assertThat(nestedMock).isNotNull();
        }

        @Test
        void rootMockCreated() {
            assertThat(rootMock).isNotNull();
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
        // ^^ duplicate registration should be ignored by JUnit
        // see http://junit.org/junit5/docs/current/user-guide/#extensions-registration-inherita
    class DuplicateExtensionOnNestedTest {

        @Mock
        Object nestedMock;

        @Test
        void ensureMocksAreCreatedForNestedTests() {
            assertThat(nestedMock).isNotNull();
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
}
