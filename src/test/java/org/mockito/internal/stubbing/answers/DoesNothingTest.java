/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.InvocationBuilder;

import static org.assertj.core.api.Assertions.assertThat;

public class DoesNothingTest {
    @Test
    public void should_do_nothing() throws Throwable {
        assertThat(new DoesNothing().answer(new InvocationBuilder().method("voidMethod").toInvocation())).isNull();
    }

    @Test(expected = MockitoException.class)
    public void should_fail_when_non_void_method_does_nothing() throws Throwable {
        new DoesNothing().validateFor(new InvocationBuilder().simpleMethod().toInvocation());
    }

    @Test
    public void should_allow_void_return_for_void_method() throws Throwable {
        new DoesNothing().validateFor(new InvocationBuilder().method("voidMethod").toInvocation());
    }
}
