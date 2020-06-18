/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.exceptions.Reporter.cannotStubWithNullThrowable;
import static org.mockito.internal.exceptions.Reporter.checkedExceptionInvalid;

import java.io.IOException;
import java.nio.charset.CharacterCodingException;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.invocation.Invocation;

public class AbstractThrowsExceptionTest {

    @Test
    public void should_raise_wanted_throwable() {
        Throwable expected = new Exception();
        AbstractThrowsException ate = instantiateFixture(expected);

        Throwable throwable = Assertions.catchThrowable(() -> ate.answer(createMethodInvocation()));
        assertNotNull("Should have raised an exception.", throwable);
        assertSame(expected, throwable);
    }

    @Test
    public void should_throw_mock_exception_without_stacktrace() {
        AbstractThrowsException ate = instantiateFixture(mock(Exception.class));

        Throwable throwable = Assertions.catchThrowable(() -> ate.answer(createMethodInvocation()));
        assertNotNull("Should have raised an exception.", throwable);
        assertThat(throwable.getStackTrace()).describedAs("no stack trace, it's mock").isNull();
    }

    @Test
    public void should_fill_in_exception_stacktrace() {
        AbstractThrowsException ate = instantiateFixture(new Exception());

        Throwable throwable = Assertions.catchThrowable(() -> ate.answer(createMethodInvocation()));
        assertNotNull("Should have raised an exception.", throwable);
        assertThat(throwable.getStackTrace()[0].getClassName())
                .isEqualTo(AbstractThrowsException.class.getName());
        assertThat(throwable.getStackTrace()[0].getMethodName()).isEqualTo("answer");
    }

    @Test
    public void should_invalidate_null_throwable() {
        AbstractThrowsException ate = instantiateFixture(null);

        Throwable throwable =
                Assertions.catchThrowableOfType(
                        () -> ate.validateFor(createMethodInvocation()), MockitoException.class);
        assertNotNull("Should have raised a MockitoException.", throwable);
        assertEquals(cannotStubWithNullThrowable().getMessage(), throwable.getMessage());
    }

    @Test
    public void should_throw_illegal_state_exception_if_null_answer() {
        AbstractThrowsException ate = instantiateFixture(null);

        Throwable throwable =
                Assertions.catchThrowableOfType(
                        () -> ate.answer(createMethodInvocation()), IllegalStateException.class);
        assertNotNull("Should have raised a IllegalStateException.", throwable);
        assertEquals(
                "throwable is null: you shall not call #answer if #validateFor fails!",
                throwable.getMessage());
    }

    @Test
    public void should_pass_proper_checked_exception() {
        instantiateFixture(new CharacterCodingException()).validateFor(createMethodInvocation());
    }

    @Test
    public void should_fail_invalid_checked_exception() {
        AbstractThrowsException ate = instantiateFixture(new IOException());
        Throwable comparison = ate.getThrowable();

        Throwable throwable =
                Assertions.catchThrowableOfType(
                        () -> ate.validateFor(createMethodInvocation()), MockitoException.class);
        assertNotNull("Should have raised a MockitoException.", throwable);
        assertEquals(checkedExceptionInvalid(comparison).getMessage(), throwable.getMessage());
    }

    @Test
    public void should_pass_RuntimeException() {
        instantiateFixture(new RuntimeException()).validateFor(createMethodInvocation());
    }

    @Test
    public void should_pass_Error() {
        instantiateFixture(new Error()).validateFor(createMethodInvocation());
    }

    /** Creates a fixture for AbstractThrowsException that returns the given Throwable. */
    private static AbstractThrowsException instantiateFixture(Throwable throwable) {
        return new AbstractThrowsException() {
            @Override
            protected Throwable getThrowable() {
                return throwable;
            }
        };
    }

    /** Creates Invocation of a "canThrowException" method call. */
    private static Invocation createMethodInvocation() {
        return new InvocationBuilder().method("canThrowException").toInvocation();
    }

    @Test
    public void fixture_should_return_expected_throwable() {
        Throwable expected = new RuntimeException();
        AbstractThrowsException ate = instantiateFixture(expected);

        assertSame(expected, ate.getThrowable());
    }
}
