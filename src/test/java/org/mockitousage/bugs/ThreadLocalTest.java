/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockitoutil.TestBase;

/**
 * This was an issue reported in #2905. Mocking {@link ThreadLocal} or classes extending {@link ThreadLocal} was
 * throwing a {@link StackOverflowError}.
 */
public class ThreadLocalTest extends TestBase {

    @Test
    public void mock_ThreadLocal_does_not_raise_StackOverflowError() {
        StackOverflowError stackOverflowError =
                Assertions.catchThrowableOfType(
                        () -> {
                            mock(ThreadLocal.class, RETURNS_MOCKS);
                        },
                        StackOverflowError.class);
        Assertions.assertThat(stackOverflowError).isNull();
    }

    @Test
    public void mock_class_extending_ThreadLocal_does_not_raise_StackOverflowError() {
        StackOverflowError stackOverflowError =
                Assertions.catchThrowableOfType(
                        () -> {
                            mock(SomeThreadLocal.class, RETURNS_MOCKS);
                        },
                        StackOverflowError.class);
        Assertions.assertThat(stackOverflowError).isNull();
    }

    @Test
    public void spy_ThreadLocal_does_not_raise_StackOverflowError() {
        StackOverflowError stackOverflowError =
                Assertions.catchThrowableOfType(
                        () -> {
                            spy(ThreadLocal.class);
                        },
                        StackOverflowError.class);
        Assertions.assertThat(stackOverflowError).isNull();
    }

    @Test
    public void spy_class_extending_ThreadLocal_does_not_raise_StackOverflowError() {
        StackOverflowError stackOverflowError =
                Assertions.catchThrowableOfType(
                        () -> {
                            spy(SomeThreadLocal.class);
                        },
                        StackOverflowError.class);
        Assertions.assertThat(stackOverflowError).isNull();
    }

    static class SomeThreadLocal<T> extends ThreadLocal<T> {}
}
