/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import java.io.IOException;
import java.nio.charset.CharacterCodingException;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.InvocationBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;


public class ThrowsExceptionTest {
    @Test
    public void should_raise_wanted_throwable() throws Throwable {
        try {
            new ThrowsException(new IllegalStateException("my dear throwable")).answer(new InvocationBuilder().method("canThrowException").toInvocation());
            Assertions.fail("should have raised wanted exception");
        } catch (Throwable throwable) {
            assertThat(throwable).isInstanceOf(IllegalStateException.class).hasMessage("my dear throwable");
        }
    }

    @Test
    public void should_throw_mock_exception_without_stacktrace() throws Exception {
        try {
            new ThrowsException(mock(Exception.class)).answer(new InvocationBuilder().method("canThrowException").toInvocation());
            Assertions.fail("should have raised wanted exception");
        } catch (Throwable throwable) {
            assertThat(throwable.getStackTrace()).describedAs("no stack trace, it's mock").isNull();
        }
    }

    @Test
    public void should_fill_in_exception_stacktrace() throws Exception {
        // given
        Exception throwableToRaise = new Exception();
        throwableToRaise.fillInStackTrace();
        assertThat(throwableToRaise.getStackTrace()[0].getClassName()).isEqualTo(this.getClass().getName());
        assertThat(throwableToRaise.getStackTrace()[0].getMethodName()).isEqualTo("should_fill_in_exception_stacktrace");
        try {

            // when
            new ThrowsException(throwableToRaise).answer(new InvocationBuilder().method("canThrowException").toInvocation());
            Assertions.fail("should have raised wanted exception");
        } catch (Throwable throwable) {
            // then
            throwable.printStackTrace();
            assertThat(throwableToRaise.getStackTrace()[0].getClassName()).isEqualTo(ThrowsException.class.getName());
            assertThat(throwableToRaise.getStackTrace()[0].getMethodName()).isEqualTo("answer");
        }
    }

    @Test
    public void should_invalidate_null_throwable() throws Throwable {
        try {
            new ThrowsException(null).validateFor(new InvocationBuilder().toInvocation());
            Assertions.fail("should have raised a MockitoException");
        } catch (MockitoException expected) {}
    }

    @Test
    public void should_pass_proper_checked_exception() throws Throwable {
        new ThrowsException(new CharacterCodingException()).validateFor(new InvocationBuilder().method("canThrowException").toInvocation());
    }

    @Test(expected = MockitoException.class)
    public void should_fail_invalid_checked_exception() throws Throwable {
        new ThrowsException(new IOException()).validateFor(new InvocationBuilder().method("canThrowException").toInvocation());
    }

    @Test
    public void should_pass_RuntimeExceptions() throws Throwable {
        new ThrowsException(new Error()).validateFor(new InvocationBuilder().method("canThrowException").toInvocation());
        new ThrowsException(new RuntimeException()).validateFor(new InvocationBuilder().method("canThrowException").toInvocation());
    }
}
