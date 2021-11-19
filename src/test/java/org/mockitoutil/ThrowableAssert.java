/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoutil;

import static org.junit.Assert.assertEquals;

/**
 * Clean asserts for exception handling
 */
public class ThrowableAssert {

    private Throwable reportedException;

    private ThrowableAssert(Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable t) {
            this.reportedException = t;
            return;
        }
        throw new AssertionError("Expected runnable to throw an exception but it didn't");
    }

    public ThrowableAssert throwsException(Class<? extends Throwable> exceptionType) {
        if (!exceptionType.isInstance(reportedException)) {
            throw new AssertionError(
                    "Exception should be of type: "
                            + exceptionType.getSimpleName()
                            + " but it was: "
                            + reportedException.getClass().getSimpleName());
        }
        return this;
    }

    public ThrowableAssert throwsMessage(String exceptionMessage) {
        assertEquals(exceptionMessage, reportedException.getMessage());
        return this;
    }

    /**
     * Executes provided runnable, expects it to throw an exception.
     * Then, it offers ways to assert on the expected exception.
     */
    public static ThrowableAssert assertThat(Runnable runnable) {
        return new ThrowableAssert(runnable);
    }
}
