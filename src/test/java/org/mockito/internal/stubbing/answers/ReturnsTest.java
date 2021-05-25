/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import static java.lang.Boolean.TRUE;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.InvocationBuilder;

public class ReturnsTest {
    @Test
    public void should_return_value() throws Throwable {
        assertThat(
                        new Returns("value")
                                .answer(
                                        new InvocationBuilder()
                                                .method("oneArg")
                                                .arg("A")
                                                .toInvocation()))
                .isEqualTo("value");
    }

    @Test(expected = MockitoException.class)
    public void should_fail_when_return_Value_is_set_for_void_method() throws Throwable {
        new Returns("one").validateFor(new InvocationBuilder().method("voidMethod").toInvocation());
    }

    @Test
    public void should_allow_correct_type_of_return_value() throws Throwable {
        new Returns("one").validateFor(new InvocationBuilder().simpleMethod().toInvocation());
        new Returns(false)
                .validateFor(
                        new InvocationBuilder().method("booleanReturningMethod").toInvocation());
        new Returns(TRUE)
                .validateFor(
                        new InvocationBuilder()
                                .method("booleanObjectReturningMethod")
                                .toInvocation());
        new Returns(1)
                .validateFor(
                        new InvocationBuilder().method("integerReturningMethod").toInvocation());
        new Returns(1L)
                .validateFor(new InvocationBuilder().method("longReturningMethod").toInvocation());
        new Returns(1L)
                .validateFor(
                        new InvocationBuilder().method("longObjectReturningMethod").toInvocation());
        new Returns(null)
                .validateFor(
                        new InvocationBuilder()
                                .method("objectReturningMethodNoArgs")
                                .toInvocation());
        new Returns(1)
                .validateFor(
                        new InvocationBuilder()
                                .method("objectReturningMethodNoArgs")
                                .toInvocation());
    }

    @Test(expected = MockitoException.class)
    public void should_fail_on_return_type_mismatch() throws Throwable {
        new Returns("String")
                .validateFor(
                        new InvocationBuilder().method("booleanReturningMethod").toInvocation());
    }

    @Test(expected = MockitoException.class)
    public void should_fail_on_wrong_primitive() throws Throwable {
        new Returns(1)
                .validateFor(
                        new InvocationBuilder().method("doubleReturningMethod").toInvocation());
    }

    @Test(expected = MockitoException.class)
    public void should_fail_on_null_with_primitive() throws Throwable {
        new Returns(null)
                .validateFor(
                        new InvocationBuilder().method("booleanReturningMethod").toInvocation());
    }
}
