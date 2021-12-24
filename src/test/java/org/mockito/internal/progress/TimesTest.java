/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.verification.VerificationModeFactory;

public class TimesTest {

    @Test
    public void shouldNotAllowNegativeNumberOfInvocations() {
        assertThatThrownBy(
                        () -> {
                            VerificationModeFactory.times(-50);
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessage("Negative value is not allowed here");
    }
}
