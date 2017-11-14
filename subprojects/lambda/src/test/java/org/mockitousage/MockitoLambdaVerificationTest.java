/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import org.junit.Test;
import org.mockito.AutoBoxingNullPointerException;

import javax.annotation.Nonnull;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.MockitoLambda.*;

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

    /**
     * Shows how #1255 can be resolved.
     */
    @Test
    public void works_with_non_null_parameters() {
        NonNullParamClass mock = mock(NonNullParamClass.class);
        mock.doSomething("toto");
        verify(mock::doSomething).invokedWith(eq("toto"));
    }

    class NonNullParamClass {
        String doSomething(@Nonnull String value) {
            return value;
        }
    }
}
