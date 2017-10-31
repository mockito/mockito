package org.mockitousage;

import org.junit.Test;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.MockitoLambda.when;
import static org.mockito.MockitoLambda.mock;
import static org.mockito.MockitoLambda.any;

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
    public void can_redefine_stub_after_exception() {
        IMethods mock = mock(IMethods.class);
        when(mock::stringReturningMethod).invokedWithNoArgs().thenThrow(new Exception("Text"));
        when(mock::stringReturningMethod).invokedWithNoArgs().thenReturn("Hello World");

        assertThat(mock.stringReturningMethod()).isEqualTo("Hello World");
    }

    @Test
    public void can_match_on_equals() {
        IMethods mock = mock(IMethods.class);
        when((Function<String, String>) mock::oneArg).invokedWith("Hello World").thenAnswer((string) -> string + "!");

        assertThat(mock.oneArg("Hello World")).isEqualTo("Hello World!");
    }

    @Test
    public void can_match_on_equals_with_primitive() {
        IMethods mock = mock(IMethods.class);
        when(mock::intArgumentReturningInt).invokedWith(5).thenAnswer((integer) -> integer + 3);

        assertThat(mock.intArgumentReturningInt(5)).isEqualTo(8);
    }
}
