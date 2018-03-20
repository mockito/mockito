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
import org.mockito.invocation.Invocation;

import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;


public class ThrowsExceptionTest {
    @Test
    public void should_raise_wanted_throwable() throws Throwable {
        try {
            new ThrowsException(new IllegalStateException("my dear throwable")).answer(createMethodInvocation());
            Assertions.fail("should have raised wanted exception");
        } catch (Throwable throwable) {
            assertThat(throwable).isInstanceOf(IllegalStateException.class).hasMessage("my dear throwable");
        }
    }

    @Test
    public void should_throw_mock_exception_without_stacktrace() throws Exception {
        try {
            new ThrowsException(mock(Exception.class)).answer(createMethodInvocation());
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
            new ThrowsException(throwableToRaise).answer(createMethodInvocation());
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
            Invocation invocation = createMethodInvocation();
            new ThrowsException(null).validateFor(invocation);
            Assertions.fail("should have raised a MockitoException");
        } catch (MockitoException expected) {}
    }

    @Test
    public void should_throw_illegal_state_exception_if_null_answer() throws Throwable {
        Invocation invocation = createMethodInvocation();
        try {
            new ThrowsException(null).answer(invocation);
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    @Test
    public void should_pass_proper_checked_exception() throws Throwable {
        new ThrowsException(new CharacterCodingException()).validateFor(createMethodInvocation());
    }

    @Test(expected = MockitoException.class)
    public void should_fail_invalid_checked_exception() throws Throwable {
        new ThrowsException(new IOException()).validateFor(createMethodInvocation());
    }

    @Test
    public void should_pass_RuntimeExceptions() throws Throwable {
        new ThrowsException(new Error()).validateFor(createMethodInvocation());
        new ThrowsException(new RuntimeException()).validateFor(createMethodInvocation());
    }

    /** Creates Invocation of a "canThrowException" method call. */
    private static Invocation createMethodInvocation() {
        return new InvocationBuilder()
            .method("canThrowException")
            .toInvocation();
    }
}
