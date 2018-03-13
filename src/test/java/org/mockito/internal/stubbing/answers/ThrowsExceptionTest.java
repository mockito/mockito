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

    /**
     * Tests that ThrowsException raises the same instance as passed in the constructor
     * in case of a well-behaved Throwable implementation.
     *
     * @see #should_raise_same_wanted_throwable_even_if_it_returns_null_in_fill_in_stack_trace()
     */
    @Test
    public void should_raise_same_wanted_throwable_on_subsequent_invocations() {
        Throwable expectedThrowable = new IllegalStateException("An expected throwable");
        ThrowsException throwingAnswer = new ThrowsException(expectedThrowable);
        int numInvocations = 2;
        for (int i = 0; i < numInvocations; i++) {
            try {
                throwingAnswer.answer(createMethodInvocation());
                Assertions.fail("Should have raised wanted exception");
            } catch (Throwable throwable) {
                // All invocations must result in the same throwable as passed to the constructor.
                assertThat(expectedThrowable).isSameAs(expectedThrowable);
            }
        }
    }

    /**
     * Tests that a custom exception that breaks the Throwable contract returning null
     * in fillInStackTrace does not break ThrowsException#answer.
     *
     * Any Throwable implementation must always return a reference to this
     * from #fillInStackTrace.
     *
     * @see Throwable#fillInStackTrace()
     * @see <a href="https://github.com/mockito/mockito/issues/866">#866</a>
     */
    @Test
    public void should_raise_same_wanted_throwable_even_if_it_returns_null_in_fill_in_stack_trace() {
        /*
         * An evil throwable implementation that breaks Throwable#fillInStackTrace contract.
         *
         * We can't use a mock here because ThrowsException#answer handles mocks of exceptions
         * differently.
         */
        class EvilThrowable extends Throwable {
            @Override public synchronized Throwable fillInStackTrace() {
                return null;
            }
        }

        Throwable expectedThrowable = new EvilThrowable();
        ThrowsException throwingAnswer = new ThrowsException(expectedThrowable);
        try {
            throwingAnswer.answer(createMethodInvocation());
            Assertions.fail("should have raised wanted exception");
        } catch (Throwable throwable) {
            assertThat(throwable).isSameAs(expectedThrowable);
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
