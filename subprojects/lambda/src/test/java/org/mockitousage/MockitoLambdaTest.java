/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import org.junit.Test;
import org.mockito.AutoBoxingNullPointerException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.MockitoLambda.*;

public class MockitoLambdaTest {

    @Test
    public void should_mock_simple_lambda() {
        IMethods mock = mock(IMethods.class);
        when(mock::stringReturningMethod).invokedWithNoArgs().thenReturn("Hello World");

        assertThat(mock.stringReturningMethod()).isEqualTo("Hello World");
    }

    @Test
    public void should_throw_simple_exception() {
        IMethods mock = mock(IMethods.class);
        Throwable e = new Exception("Text");
        when(mock::stringReturningMethod).invokedWithNoArgs().thenThrow(e);

        assertThatThrownBy(mock::stringReturningMethod).isSameAs(e);
    }

    @Test
    public void should_invoke_simple_answer() {
        IMethods mock = mock(IMethods.class);
        when((Function<String, String>) mock::oneArg).invokedWith(any(String.class)).thenAnswer((string) -> string + "!");

        assertThat(mock.oneArg("Hello World")).isEqualTo("Hello World!");
    }

    @Test
    public void should_not_invoke_answer_if_any_does_not_match() {
        IMethods mock = mock(IMethods.class);
        when(mock::objectArgMethod).invokedWith(any(String.class)).thenAnswer((string) -> string + "!");

        assertThat(mock.objectArgMethod(5)).isEqualTo(null);
    }

    @Test
    public void should_invoke_old_answer() {
        IMethods mock = mock(IMethods.class);
        when((Function<String, String>) mock::oneArg).invokedWith(any(String.class)).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgument(0);
            }
        });

        assertThat(mock.oneArg("Hello World")).isEqualTo("Hello World");
    }

    @Test
    public void should_invoke_stub_with_any_args() {
        IMethods mock = mock(IMethods.class);
        when((Function<String, String>) mock::oneArg).invokedWithAnyArgs().thenAnswer((string) -> string + "!");

        assertThat(mock.oneArg("Hello World")).isEqualTo("Hello World!");
    }

    @Test
    public void should_invoke_stub_with_any_args_primitives() {
        IMethods mock = mock(IMethods.class);

        assertThatThrownBy(() -> when(mock::intArgumentReturningInt).invokedWithAnyArgs().thenReturn(3))
            .isInstanceOf(AutoBoxingNullPointerException.class)
            .hasMessageContaining("use `any()` or `invokedWithAnyArgs()`");
    }

    /**
     * Shows how #1237 can be resolved.
     */
    @Test
    public void can_redefine_stub_after_exception() {
        IMethods mock = mock(IMethods.class);
        when(mock::stringReturningMethod).invokedWithNoArgs().thenThrow(new Exception("Text"));
        when(mock::stringReturningMethod).invokedWithNoArgs().thenReturn("Hello World");

        assertThat(mock.stringReturningMethod()).isEqualTo("Hello World");
    }

    @Test
    public void can_match_on_equals_matcher() {
        IMethods mock = mock(IMethods.class);
        when((Function<String, String>) mock::oneArg).invokedWith("Hello World").thenAnswer((string) -> string + "!");

        assertThat(mock.oneArg("Hello World")).isEqualTo("Hello World!");
    }

    @Test
    public void can_match_on_equals_with_primitive_matcher() {
        IMethods mock = mock(IMethods.class);
        when(mock::intArgumentReturningInt).invokedWith(5).thenAnswer((integer) -> integer + 3);

        assertThat(mock.intArgumentReturningInt(5)).isEqualTo(8);
    }

    @Test
    public void can_match_on_equals_with_null_matcher() {
        IMethods mock = mock(IMethods.class);
        when((Function<String, String>) mock::differentMethod).invokedWith(isNull()).thenReturn("Was null");

        assertThat(mock.differentMethod(null)).isEqualTo("Was null");
    }

    @Test
    public void can_match_on_equals_with_eq_null_matcher() {
        IMethods mock = mock(IMethods.class);
        when((Function<String, String>) mock::differentMethod).invokedWith(eq(null)).thenReturn("Was null");

        assertThat(mock.differentMethod(null)).isEqualTo("Was null");
    }

    @Test
    public void does_not_throw_when_null_passed_in_equals_matcher() {
        IMethods mock = mock(IMethods.class);
        when((Function<String, String>) mock::differentMethod).invokedWith("Hello World").thenAnswer((string) -> string + "!");

        assertThat(mock.differentMethod(null)).isEqualTo(null);
    }

    /**
     * Shows how #1174 can be resolved.
     */
    @Test
    public void can_handle_generic_return_type_based_on_method_type_parameter() {
        TestCollectionSourceProvider testCollectionSourceProvider = mock(TestCollectionSourceProvider.class);
        when((Function<ArrayList<Integer>, ArrayList<Integer>>) testCollectionSourceProvider::getCollection).invokedWith(new ArrayList<>()).thenReturn(new ArrayList<>());
    }

    @Test
    public void should_clean_up_matchers_after_only_stubbing() {
        TestCollectionSourceProvider testCollectionSourceProvider = mock(TestCollectionSourceProvider.class);
        when((Function<ArrayList<Integer>, ArrayList<Integer>>) testCollectionSourceProvider::getCollection).invokedWith(new ArrayList<>()).thenReturn(new ArrayList<>());

        IMethods mock2 = mock(IMethods.class);
        when(mock2::stringReturningMethod).invokedWithNoArgs().thenReturn("Hello World");
    }

    @Test
    public void should_verify_simple_lambda() {
        IMethods mock = mock(IMethods.class);
        when(mock::stringReturningMethod).invokedWithNoArgs().thenReturn("Hello World");

        mock.stringReturningMethod();

        verify(mock::stringReturningMethod).invokedWithNoArgs();
    }

    @Test
    public void should_verify_simple_lambda_with_any_args() {
        IMethods mock = mock(IMethods.class);
        when(mock::intArgumentReturningInt).invokedWith(5).thenReturn(3);

        mock.intArgumentReturningInt(5);

        assertThatThrownBy(() -> verify(mock::intArgumentReturningInt).invokedWithAnyArgs())
            .isInstanceOf(AutoBoxingNullPointerException.class)
            .hasMessageContaining("use `any()` or `invokedWithAnyArgs()`");
    }

    class TestCollectionSourceProvider {
        <T extends Collection<E>, E> T getCollection(T collection) {
            return collection;
        }
    }
}
