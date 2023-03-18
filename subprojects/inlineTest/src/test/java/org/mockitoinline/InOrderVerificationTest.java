/*
 * Copyright (c) 2022 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;

import org.assertj.core.api.Assert;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.MockedStatic;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.exceptions.verification.VerificationInOrderFailure;
import org.mockito.verification.VerificationMode;

public class InOrderVerificationTest {
    @Test
    public void shouldVerifyStaticMethods() {
        try (MockedStatic<StaticContext> mockedStatic = mockStatic(StaticContext.class)) {
            // given
            InOrder inOrder = inOrder(StaticContext.class);

            // when
            StaticContext.firstMethod();
            StaticContext.secondMethod(0);

            // then
            inOrder.verify(mockedStatic, StaticContext::firstMethod);
            inOrder.verify(mockedStatic, () -> StaticContext.secondMethod(0));
        }
    }

    @Test
    public void shouldVerifyStaticAndInstanceMethods() {
        try (MockedStatic<StaticContext> mockedStatic = mockStatic(StaticContext.class)) {
            // given
            StaticContext mocked = mock(StaticContext.class);
            InOrder inOrder = inOrder(mocked, StaticContext.class);

            // when
            StaticContext.firstMethod();
            mocked.instanceMethod();
            StaticContext.secondMethod(10);

            // then
            inOrder.verify(mockedStatic, StaticContext::firstMethod);
            inOrder.verify(mocked).instanceMethod();
            inOrder.verify(mockedStatic, () -> StaticContext.secondMethod(10));
        }
    }

    @Test
    public void shouldVerifyStaticMethodsWithSimpleAndWrapperModes() {
        try (MockedStatic<StaticContext> mockedStatic = mockStatic(StaticContext.class)) {
            // given
            InOrder inOrder = inOrder(StaticContext.class);

            // when
            StaticContext.firstMethod();
            StaticContext.firstMethod();
            StaticContext.secondMethod(0);

            // then
            inOrder.verify(mockedStatic, StaticContext::firstMethod, times(2));
            inOrder.verify(mockedStatic, () -> StaticContext.secondMethod(0), timeout(100).atLeastOnce());
        }
    }

    @Test
    public void shouldThrowExceptionWhenModeIsUnsupported() {
        try (MockedStatic<StaticContext> mockedStatic = mockStatic(StaticContext.class)) {
            // given
            VerificationMode unsupportedMode = data -> { };
            InOrder inOrder = inOrder(StaticContext.class);

            // when
            StaticContext.firstMethod();

            // then
            assertThatThrownBy(() ->
                inOrder.verify(mockedStatic, StaticContext::firstMethod, unsupportedMode)
            ).isInstanceOf(MockitoException.class);
        }
    }

    @Test
    public void shouldThrowExceptionWhenOrderIsWrong() {
        try (MockedStatic<StaticContext> mockedStatic = mockStatic(StaticContext.class)) {
            // given
            InOrder inOrder = inOrder(StaticContext.class);

            // when
            StaticContext.firstMethod();
            StaticContext.secondMethod(0);

            // then
            assertThatThrownBy(() -> {
                inOrder.verify(mockedStatic, () -> StaticContext.secondMethod(0));
                inOrder.verify(mockedStatic, StaticContext::firstMethod);
            }).isInstanceOf(VerificationInOrderFailure.class);
        }
    }

    @Test
    public void shouldThrowExceptionWhenNoMoreInteractionsInvokedButThereAre() {
        try (MockedStatic<StaticContext> mockedStatic = mockStatic(StaticContext.class)) {
            // given
            InOrder inOrder = inOrder(StaticContext.class);

            // when
            StaticContext.firstMethod();
            StaticContext.secondMethod(0);

            // then
            inOrder.verify(mockedStatic, StaticContext::firstMethod);
            assertThatThrownBy(inOrder::verifyNoMoreInteractions)
                .isInstanceOf(VerificationInOrderFailure.class);
        }
    }

    @Test
    public void shouldThrowExceptionWhenNoMoreInteractionsInvokedWithoutVerifyingStaticMethods() {
        try (MockedStatic<StaticContext> ignored = mockStatic(StaticContext.class)) {
            // given
            StaticContext mocked = mock(StaticContext.class);
            InOrder inOrder = inOrder(StaticContext.class, mocked);

            // when
            mocked.instanceMethod();
            StaticContext.firstMethod();

            // then
            inOrder.verify(mocked).instanceMethod();
            assertThatThrownBy(inOrder::verifyNoMoreInteractions)
                .isInstanceOf(VerificationInOrderFailure.class);
        }
    }

    @Test
    public void shouldThrowExceptionWhenClassIsNotMocked() {
        assertThatThrownBy(
            () -> inOrder(StaticContext.class)
        ).isInstanceOf(NotAMockException.class);
    }

    @Test
    public void shouldVerifyStaticMethodsWithoutInterferingWithMocking() {
        try (MockedStatic<StaticContext> mockedStatic = mockStatic(StaticContext.class)) {
            // given
            InOrder inOrder = inOrder(StaticContext.class);
            Exception expected = new RuntimeException();
            mockedStatic.when(StaticContext::firstMethod).thenThrow(expected);

            // when
            Assert<?, ?> actual = assertThatThrownBy(StaticContext::firstMethod);

            // then
            actual.isSameAs(expected);
            inOrder.verify(mockedStatic, StaticContext::firstMethod);
            inOrder.verifyNoMoreInteractions();
        }
    }

    @Test
    public void shouldThrowExceptionWhenVerifyUsingInOrderWithoutValidClass() {
        try (MockedStatic<StaticContext> mockedStaticContext = mockStatic(StaticContext.class)) {
            try (MockedStatic<AnotherStaticContext> mockedAnotherStaticContext = mockStatic(AnotherStaticContext.class)) {
                // given
                InOrder inOrder = inOrder(AnotherStaticContext.class);

                // when
                mockedAnotherStaticContext.when(AnotherStaticContext::otherMethod).thenReturn("mocked value");
                StaticContext.firstMethod();

                // then
                assertThat(AnotherStaticContext.otherMethod())
                    .isEqualTo("mocked value");
                inOrder.verify(mockedAnotherStaticContext, AnotherStaticContext::otherMethod);
                assertThatThrownBy(() -> inOrder.verify(mockedStaticContext, StaticContext::firstMethod))
                    .isInstanceOf(VerificationInOrderFailure.class);
            }
        }
    }

    private static class AnotherStaticContext {
        static String otherMethod() {
            throw new AssertionError("otherMethod should be mocked");
        }
    }

    private static class StaticContext {
        static void firstMethod() {
            fail("firstMethod should be mocked");
        }

        static void secondMethod(int n) {
            fail("secondMethod should be mocked but was invoked with argument " + n);
        }

        void instanceMethod() {
            fail("instanceMethod should be mocked");
        }
    }
}
