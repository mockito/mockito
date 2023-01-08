/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.misuse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Assume;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.MissingMethodInvocationException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.plugins.InlineMockMaker;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class InvalidUsageTest extends TestBase {

    @Mock private IMethods mock;
    @Mock private IMethods mockTwo;

    @After
    public void resetState() {
        super.resetState();
    }

    @Test
    public void shouldRequireArgumentsWhenVerifyingNoMoreInteractions() {
        assertThatThrownBy(
                        () -> {
                            verifyNoMoreInteractions();
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "Method requires argument(s)!",
                        "Pass mocks that should be verified, e.g:",
                        "    verifyNoMoreInteractions(mockOne, mockTwo);",
                        "    verifyNoInteractions(mockOne, mockTwo);");
    }

    @Test
    public void shouldRequireArgumentsWhenVerifyingNoInteractions() {
        assertThatThrownBy(
                        () -> {
                            verifyNoInteractions();
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "Method requires argument(s)!",
                        "Pass mocks that should be verified, e.g:",
                        "    verifyNoMoreInteractions(mockOne, mockTwo);",
                        "    verifyNoInteractions(mockOne, mockTwo);");
    }

    @SuppressWarnings({"CheckReturnValue", "MockitoUsage"})
    @Test
    public void shouldNotCreateInOrderObjectWithoutMocks() {
        assertThatThrownBy(
                        () -> {
                            inOrder();
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "Method requires argument(s)!",
                        "Pass mocks that require verification in order.",
                        "For example:",
                        "    InOrder inOrder = inOrder(mockOne, mockTwo);");
    }

    @Test
    public void shouldNotAllowVerifyingInOrderUnfamiliarMocks() {
        InOrder inOrder = inOrder(mock);
        assertThatThrownBy(
                        () -> {
                            inOrder.verify(mockTwo).simpleMethod();
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "InOrder can only verify mocks that were passed in during creation of InOrder.",
                        "For example:",
                        "    InOrder inOrder = inOrder(mockOne);",
                        "    inOrder.verify(mockOne).doStuff();");
    }

    @Test
    public void shouldReportMissingMethodInvocationWhenStubbing() {
        when(mock.simpleMethod())
                .thenReturn("this stubbing is required to make sure Stubbable is pulled");
        assertThatThrownBy(
                        () -> {
                            when("".toString()).thenReturn("x");
                        })
                .isInstanceOf(MissingMethodInvocationException.class)
                .hasMessageContainingAll(
                        "when() requires an argument which has to be 'a method call on a mock'.",
                        "For example:",
                        "    when(mock.getArticles()).thenReturn(articles);",
                        "Also, this error might show up because:",
                        "1. you stub either of: final/private/equals()/hashCode() methods.",
                        "   Those methods *cannot* be stubbed/verified.",
                        "   Mocking methods declared on non-public parent classes is not supported.",
                        "2. inside when() you don't call method on mock but on some other object.");
    }

    @Test
    public void shouldNotAllowSettingInvalidCheckedException() {
        assertThatThrownBy(
                        () -> {
                            when(mock.simpleMethod()).thenThrow(new Exception());
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "Checked exception is invalid for this method!",
                        "Invalid: java.lang.Exception");
    }

    @Test
    public void shouldNotAllowSettingNullThrowable() {
        assertThatThrownBy(
                        () -> {
                            when(mock.simpleMethod()).thenThrow(new Throwable[] {null});
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("Cannot stub with null throwable!");
    }

    @SuppressWarnings("all")
    @Test
    public void shouldNotAllowSettingNullThrowableVararg() throws Exception {
        assertThatThrownBy(
                        () -> {
                            when(mock.simpleMethod()).thenThrow((Throwable) null);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("Cannot stub with null throwable!");
    }

    @Test
    public void shouldNotAllowSettingNullConsecutiveThrowable() {
        assertThatThrownBy(
                        () -> {
                            when(mock.simpleMethod()).thenThrow(new RuntimeException(), null);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("Cannot stub with null throwable!");
    }

    final class FinalClass {}

    @Test
    public void shouldNotAllowMockingFinalClassesIfDisabled() {
        Assume.assumeThat(Plugins.getMockMaker(), not(instanceOf(InlineMockMaker.class)));

        assertThatThrownBy(
                        () -> {
                            mock(FinalClass.class);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "Cannot mock/spy class org.mockitousage.misuse.InvalidUsageTest$FinalClass",
                        "Mockito cannot mock/spy because :",
                        " - final class");
    }

    @Test
    public void shouldAllowMockingFinalClassesIfEnabled() {
        Assume.assumeThat(Plugins.getMockMaker(), instanceOf(InlineMockMaker.class));
        assertThat(mock(FinalClass.class)).isInstanceOf(FinalClass.class);
    }

    @SuppressWarnings({"CheckReturnValue", "MockitoUsage"})
    @Test
    public void shouldNotAllowMockingPrimitives() {
        assertThatThrownBy(
                        () -> {
                            mock(Integer.TYPE);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "Cannot mock/spy int",
                        "Mockito cannot mock/spy because :",
                        " - primitive type");
    }

    interface ObjectLikeInterface {
        boolean equals(Object o);

        String toString();

        int hashCode();
    }

    @Test
    public void shouldNotMockObjectMethodsOnInterfaceVerifyNoInteractions() {
        ObjectLikeInterface inter = mock(ObjectLikeInterface.class);

        Object ignored = inter.equals(null);
        ignored = inter.toString();
        ignored = inter.hashCode();

        verifyNoInteractions(inter);
    }

    @Test
    public void shouldNotMockObjectMethodsOnClassVerifyNoInteractions() {
        Object clazz = mock(ObjectLikeInterface.class);

        Object ignored = clazz.equals(null);
        ignored = clazz.toString();
        ignored = clazz.hashCode();

        verifyNoInteractions(clazz);
    }
}
