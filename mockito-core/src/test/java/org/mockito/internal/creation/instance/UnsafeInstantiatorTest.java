/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.instance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.Test;
import org.mockito.creation.instance.InstantiationException;

public class UnsafeInstantiatorTest {

    @Test
    public void allocates_without_invoking_the_constructor() {
        assertThat(UnsafeInstantiator.INSTANCE.newInstance(WithConstructor.class).value).isZero();
    }

    @Test
    public void reports_a_type_that_cannot_be_allocated() {
        assertThatExceptionOfType(InstantiationException.class)
                .isThrownBy(() -> UnsafeInstantiator.INSTANCE.newInstance(Runnable.class))
                .withMessage("Unable to instantiate java.lang.Runnable")
                .withCauseInstanceOf(java.lang.InstantiationException.class);
    }

    @Test
    public void propagates_class_initialization_errors() {
        assertThatExceptionOfType(AssertionError.class)
                .isThrownBy(
                        () -> UnsafeInstantiator.INSTANCE.newInstance(FailingInitialization.class))
                .withMessage("Class initialization failed");
    }

    private static class WithConstructor {
        private final int value;

        private WithConstructor(int value) {
            this.value = value;
        }
    }

    private static class FailingInitialization {
        static {
            failInitialization();
        }

        private static void failInitialization() {
            throw new AssertionError("Class initialization failed");
        }
    }
}
