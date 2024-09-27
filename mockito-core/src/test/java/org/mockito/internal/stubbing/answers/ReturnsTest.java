/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    public void should_fail_when_return_Value_is_set_for_void_method() {
        assertThatThrownBy(
                        () -> {
                            new Returns("one")
                                    .validateFor(
                                            new InvocationBuilder()
                                                    .method("voidMethod")
                                                    .toInvocation());
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining(
                        "'voidMethod' is a *void method* and it *cannot* be stubbed with a *return value*!");
    }

    @Test
    public void should_allow_correct_type_of_return_value() {
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

    @Test
    public void should_fail_on_return_type_mismatch() {
        assertThatThrownBy(
                        () -> {
                            new Returns("String")
                                    .validateFor(
                                            new InvocationBuilder()
                                                    .method("booleanReturningMethod")
                                                    .toInvocation());
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "String cannot be returned by booleanReturningMethod()",
                        "booleanReturningMethod() should return boolean");
    }

    @Test
    public void should_fail_on_wrong_primitive() {
        assertThatThrownBy(
                        () -> {
                            new Returns(1)
                                    .validateFor(
                                            new InvocationBuilder()
                                                    .method("doubleReturningMethod")
                                                    .toInvocation());
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "Integer cannot be returned by doubleReturningMethod()",
                        "doubleReturningMethod() should return double");
    }

    @Test
    public void should_fail_on_null_with_primitive() {
        assertThatThrownBy(
                        () -> {
                            new Returns(null)
                                    .validateFor(
                                            new InvocationBuilder()
                                                    .method("booleanReturningMethod")
                                                    .toInvocation());
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "null cannot be returned by booleanReturningMethod()",
                        "booleanReturningMethod() should return boolean");
    }
}
