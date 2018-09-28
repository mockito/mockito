/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.Supplier;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Consumer;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JunitJupiterTest {

    @Mock
    private Function<Integer, String> rootMock;

    @Captor
    private ArgumentCaptor<Integer> rootCaptor;

    @InjectMocks
    private ClassWithDependency classWithDependency;

    @Spy
    private Consumer<Integer> rootSpy = new IgnoringConsumer<>();

    @Test
    void ensure_creation_works() {
        assertThat(rootMock).isNotNull();
    }

    @Test
    void ensure_captor_creation_works() {
        assertThat(rootCaptor).isNotNull();
    }

    @Test
    void ensure_spy_creation_works() {
        assertThat(MockUtil.isSpy(rootSpy)).isTrue();
    }

    @Test
    void can_set_stubs_on_initialized_mock() {
        Mockito.when(rootMock.apply(10)).thenReturn("Return");
        assertThat(rootMock.apply(10)).isEqualTo("Return");
    }

    @Test
    void can_use_initialized_root_captor() {
        rootSpy.accept(42);
        Mockito.verify(rootSpy).accept(rootCaptor.capture());
        assertThat(rootCaptor.getValue()).isEqualTo(42);
    }

    @Test
    void initializes_parameters(@Mock Function<String, String> localMock) {
        Mockito.when(localMock.apply("Para")).thenReturn("Meter");
        assertThat(localMock.apply("Para")).isEqualTo("Meter");
    }

    @Test
    void initializes_captor_parameter(ArgumentCaptor<Integer> localCaptor) {
        rootSpy.accept(42);
        Mockito.verify(rootSpy).accept(localCaptor.capture());
        assertThat(localCaptor.getValue()).isEqualTo(42);
    }

    @Test
    void initializes_parameters_with_custom_configuration(@Mock(name = "overriddenName") Function<String, String> localMock) {
        assertThat(MockUtil.getMockName(localMock).toString()).isEqualTo("overriddenName");
    }

    @Nested
    class NestedTestWithMockConstructorParameter {

        private final Function<Integer, String> constructorMock;

        NestedTestWithMockConstructorParameter(@Mock Function<Integer, String> constructorMock) {
            this.constructorMock = constructorMock;
        }

        @Test
        void can_inject_into_constructor_parameter() {
            Mockito.when(constructorMock.apply(42)).thenReturn("42");
            assertThat(constructorMock.apply(42)).isEqualTo("42");
        }
    }

    @Nested
    class NestedTestWithArgumentCaptorConstructorParameter {

        private final ArgumentCaptor<Integer> constructorCaptor;

        NestedTestWithArgumentCaptorConstructorParameter(ArgumentCaptor<Integer> constructorCaptor) {
            this.constructorCaptor = constructorCaptor;
        }

        @Test
        void can_inject_into_constructor_parameter() {
            rootSpy.accept(42);
            Mockito.verify(rootSpy).accept(constructorCaptor.capture());
            assertThat(constructorCaptor.getValue()).isEqualTo(42);
        }

    }

    @Nested
    class NestedTestWithExtraFields {

        @Mock
        private Runnable nestedMock;

        @Captor
        private ArgumentCaptor<Consumer<Runnable>> nestedCaptor;

        @Spy
        private Consumer<Runnable> nestedSpy = new IgnoringConsumer<>();

        @Test
        void nested_mock_created() {
            assertThat(nestedMock).isNotNull();
        }

        @Test
        void root_mock_created() {
            assertThat(rootMock).isNotNull();
        }

        @Test
        void nested_captor_created() {
            assertThat(nestedCaptor).isNotNull();
        }

        @Test
        void root_captor_created() {
            assertThat(rootCaptor).isNotNull();
        }

        @Test
        void nested_spy_created() {
            assertThat(MockUtil.isSpy(nestedSpy)).isTrue();
        }

        @Test
        void root_spy_created() {
            assertThat(MockUtil.isSpy(rootSpy)).isTrue();
        }

    }

    @Nested
    @ExtendWith(MockitoExtension.class)
        // ^^ duplicate registration should be ignored by JUnit
        // see http://junit.org/junit5/docs/current/user-guide/#extensions-registration-inherita
    class DuplicateExtensionOnNestedTest {

        @Mock
        private Object nestedMock;

        @Captor
        private ArgumentCaptor<Consumer<Supplier<String>>> nestedCaptor;

        @Spy
        private Consumer<Supplier<String>> nestedSpy = new IgnoringConsumer<>();

        @Test
        void ensure_mocks_are_created_for_nested_tests() {
            assertThat(nestedMock).isNotNull();
        }

        @Test
        void ensure_captors_are_created_for_nested_tests() {
            assertThat(nestedCaptor).isNotNull();
        }

        @Test
        void ensure_spies_are_created_for_nested_tests() {
            assertThat(MockUtil.isSpy(nestedSpy)).isTrue();
        }

    }

    @Nested
    // mock is initialized by mockito session
    class NestedWithoutExtraMock {

        @Test
        void should_create_mocks_in_the_parent_context() {
            assertThat(rootMock).isNotNull();
        }

        @Test
        void should_create_captors_in_the_parent_context() {
            assertThat(rootCaptor).isNotNull();
        }

        @Test
        void should_create_spies_in_the_parent_context() {
            assertThat(MockUtil.isSpy(rootSpy)).isTrue();
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

    private static class IgnoringConsumer<T> implements Consumer<T> {

        @Override
        public void accept(final T ignore) {
        }

    }
}
