/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class ThrowsExceptionForClassTypeTest {
    @Test
    public void should_return_throwable_of_expected_class() {
        ThrowsExceptionForClassType throwsExceptionForClassType =
                new ThrowsExceptionForClassType(Exception.class);

        assertSame(Exception.class, throwsExceptionForClassType.getThrowable().getClass());
    }

    @Test
    public void should_return_different_throwables() {
        ThrowsExceptionForClassType throwsExceptionForClassType =
                new ThrowsExceptionForClassType(Exception.class);

        Throwable first = throwsExceptionForClassType.getThrowable();
        Throwable second = throwsExceptionForClassType.getThrowable();
        assertNotSame(first, second);
    }
}
