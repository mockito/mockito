/*
 * Copyright (c) 2022 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;

import java.util.function.Consumer;

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
        mockedStaticTest(mockedStatic -> {
            // given
            InOrder inOrder = inOrder(StaticContext.class);

            // when
            StaticContext.firstMethod();
            StaticContext.secondMethod(0);

            // then
            inOrder.verify(mockedStatic, StaticContext::firstMethod);
            inOrder.verify(mockedStatic, () -> StaticContext.secondMethod(0));
        });
    }

    @Test
    public void shouldVerifyStaticAndInstanceMethods() {
        mockedStaticTest(mockedStatic -> {
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
        });
    }

    @Test
    public void shouldVerifyStaticMethodsWithSimpleAndWrapperModes() {
        mockedStaticTest(mockedStatic -> {
            // given
            InOrder inOrder = inOrder(StaticContext.class);

            // when
            StaticContext.firstMethod();
            StaticContext.firstMethod();
            StaticContext.secondMethod(0);

            // then
            inOrder.verify(mockedStatic, StaticContext::firstMethod, times(2));
            inOrder.verify(mockedStatic, () -> StaticContext.secondMethod(0), timeout(100).atLeastOnce());
        });
    }

    @Test(expected = MockitoException.class)
    public void shouldThrowExceptionWhenModeIsUnsupported() {
        mockedStaticTest(mockedStatic -> {
            // given
            VerificationMode unsupportedMode = data -> { };
            InOrder inOrder = inOrder(StaticContext.class);

            // when
            StaticContext.firstMethod();

            // then
            inOrder.verify(mockedStatic, StaticContext::firstMethod, unsupportedMode);
        });
    }

    @Test(expected = VerificationInOrderFailure.class)
    public void shouldThrowExceptionWhenOrderIsWrong() {
        mockedStaticTest(mockedStatic -> {
            // given
            InOrder inOrder = inOrder(StaticContext.class);

            // when
            StaticContext.firstMethod();
            StaticContext.secondMethod(0);

            // then
            inOrder.verify(mockedStatic, () -> StaticContext.secondMethod(0));
            inOrder.verify(mockedStatic, StaticContext::firstMethod);
        });
    }

    @Test(expected = VerificationInOrderFailure.class)
    public void shouldThrowExceptionWhenNoMoreInteractionsInvokedButThereAre() {
        mockedStaticTest(mockedStatic -> {
            // given
            InOrder inOrder = inOrder(StaticContext.class);

            // when
            StaticContext.firstMethod();
            StaticContext.secondMethod(0);

            // then
            inOrder.verify(mockedStatic, StaticContext::firstMethod);
            inOrder.verifyNoMoreInteractions();
        });
    }

    @Test(expected = VerificationInOrderFailure.class)
    public void shouldThrowExceptionWhenNoMoreInteractionsInvokedWithoutVerifyingStaticMethods() {
        mockedStaticTest(ignored -> {
            // given
            StaticContext mocked = mock(StaticContext.class);
            InOrder inOrder = inOrder(StaticContext.class, mocked);

            // when
            mocked.instanceMethod();
            StaticContext.firstMethod();

            // then
            inOrder.verify(mocked).instanceMethod();
            inOrder.verifyNoMoreInteractions();
        });
    }

    @Test(expected = NotAMockException.class)
    public void shouldThrowExceptionWhenClassIsNotMocked() {
        InOrder ignored = inOrder(StaticContext.class);
    }

    @Test
    public void shouldVerifyStaticMethodsWithoutInterferingWithMocking() {
        mockedStaticTest(mockedStatic -> {
            // given
            InOrder inOrder = inOrder(StaticContext.class);
            Exception expected = new RuntimeException();
            Exception actual = null;
            mockedStatic.when(StaticContext::firstMethod).thenThrow(expected);

            // when
            try {
                StaticContext.firstMethod();
            } catch (Exception e) {
                actual = e;
            }

            // then
            if (actual != expected) {
                fail("Unable to mock static method");
            }
            inOrder.verify(mockedStatic, StaticContext::firstMethod);
            inOrder.verifyNoMoreInteractions();
        });
    }

    private void mockedStaticTest(Consumer<MockedStatic<StaticContext>> body) {
        try (MockedStatic<StaticContext> mockedStatic = mockStatic(StaticContext.class)) {
            body.accept(mockedStatic);
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
