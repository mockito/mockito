/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.misuse;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.exceptions.misusing.NullInsteadOfMockException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class DescriptiveMessagesOnMisuseTest extends TestBase {

    @Mock private IMethods mock;

    class Foo {
        public final String finalMethod() {
            return null;
        }
    }

    @SuppressWarnings("all")
    @Test
    public void tryDescriptiveMessagesOnMisuse() {
        Foo foo = mock(Foo.class);

        //        when(foo.finalMethod()).thenReturn("foo");
        //        doReturn("foo").when(foo).finalMethod();
        //        verify(foo).finalMethod();

        //        doReturn("foo");
        //        doReturn("bar");

        //        verifyNoMoreInteractions();
        //        verifyNoMoreInteractions(null);
        //        verifyNoMoreInteractions("");
        //
        //        inOrder();
        //        inOrder(null);
        //        inOrder("test");
        //        InOrder inOrder = inOrder(mock(List.class));
        //        inOrder.verify(mock).simpleMethod();

        //        verify(null);
        //        verify(mock.booleanReturningMethod());

        //        verify(mock).varargs("test", anyString());

        //        when("x").thenReturn("x");

        //        when(mock.simpleMethod());
        //        when(mock.differentMethod()).thenReturn("");
    }

    @SuppressWarnings({"MockitoUsage", "CheckReturnValue"})
    @Test
    public void shouldScreamWhenWholeMethodPassedToVerify() {
        assertThatThrownBy(
                        () -> {
                            verify(mock.booleanReturningMethod());
                        })
                .isInstanceOf(NotAMockException.class)
                .hasMessageContainingAll(
                        "Argument passed to verify() is of type Boolean and is not a mock!",
                        "Make sure you place the parenthesis correctly!",
                        "See the examples of correct verifications:",
                        "    verify(mock).someMethod();",
                        "    verify(mock, times(10)).someMethod();",
                        "    verify(mock, atLeastOnce()).someMethod();");
    }

    @Test
    public void shouldScreamWhenWholeMethodPassedToVerifyNoMoreInteractions() {
        assertThatThrownBy(
                        () -> {
                            verifyNoMoreInteractions(mock.byteReturningMethod());
                        })
                .isInstanceOf(NotAMockException.class)
                .hasMessageContainingAll(
                        "Argument(s) passed is not a mock!",
                        "Examples of correct verifications:",
                        "    verifyNoMoreInteractions(mockOne, mockTwo);",
                        "    verifyNoInteractions(mockOne, mockTwo);");
    }

    @SuppressWarnings({"CheckReturnValue", "MockitoUsage"})
    @Test
    public void shouldScreamWhenInOrderCreatedWithDodgyMock() {
        assertThatThrownBy(
                        () -> {
                            inOrder("not a mock");
                        })
                .isInstanceOf(NotAMockException.class)
                .hasMessageContainingAll(
                        "Argument(s) passed is not a mock!",
                        "Pass mocks that require verification in order.",
                        "For example:",
                        "    InOrder inOrder = inOrder(mockOne, mockTwo);");
    }

    @SuppressWarnings({"CheckReturnValue", "MockitoUsage"})
    @Test
    public void shouldScreamWhenInOrderCreatedWithNulls() {
        assertThatThrownBy(
                        () -> {
                            inOrder(mock, null);
                        })
                .isInstanceOf(NullInsteadOfMockException.class)
                .hasMessageContainingAll(
                        "Argument(s) passed is null!",
                        "Pass mocks that require verification in order.",
                        "For example:",
                        "    InOrder inOrder = inOrder(mockOne, mockTwo);");
    }

    @SuppressWarnings({"MockitoUsage", "CheckReturnValue"})
    @Test
    public void shouldScreamNullPassedToVerify() {
        assertThatThrownBy(
                        () -> {
                            verify(null);
                        })
                .isInstanceOf(NullInsteadOfMockException.class)
                .hasMessageContainingAll(
                        "Argument passed to verify() should be a mock but is null!",
                        "Examples of correct verifications:",
                        "    verify(mock).someMethod();",
                        "    verify(mock, times(10)).someMethod();",
                        "    verify(mock, atLeastOnce()).someMethod();",
                        "    not: verify(mock.someMethod());",
                        "Also, if you use @Mock annotation don't miss openMocks()");
    }

    @Test
    public void shouldScreamWhenNotMockPassedToVerifyNoMoreInteractions() {
        assertThatThrownBy(
                        () -> {
                            verifyNoMoreInteractions(null, "blah");
                        })
                .isInstanceOf(NullInsteadOfMockException.class)
                .hasMessageContainingAll(
                        "Argument(s) passed is null!",
                        "Examples of correct verifications:",
                        "    verifyNoMoreInteractions(mockOne, mockTwo);",
                        "    verifyNoInteractions(mockOne, mockTwo);");
    }

    @SuppressWarnings("all")
    @Test
    public void shouldScreamWhenNullPassedToVerifyNoMoreInteractions() {
        assertThatThrownBy(
                        () -> {
                            verifyNoMoreInteractions((Object[]) null);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "Method requires argument(s)!",
                        "Pass mocks that should be verified, e.g:",
                        "    verifyNoMoreInteractions(mockOne, mockTwo);",
                        "    verifyNoInteractions(mockOne, mockTwo);");
    }
}
