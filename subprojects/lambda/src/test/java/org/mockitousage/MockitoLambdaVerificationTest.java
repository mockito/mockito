package org.mockitousage;

import org.junit.Test;
import org.mockito.AutoBoxingNullPointerException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.MockitoLambda.mock;
import static org.mockito.MockitoLambda.verify;
import static org.mockito.MockitoLambda.when;

public class MockitoLambdaVerificationTest {

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
}
