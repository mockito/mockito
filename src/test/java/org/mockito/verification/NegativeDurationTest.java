/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.verification;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.FriendlyReminderException;

public class NegativeDurationTest {

    @Test
    public void should_throw_exception_when_duration_is_negative_for_timeout_method() {
        assertThatThrownBy(
                        () -> {
                            Mockito.timeout(-1);
                        })
                .isInstanceOf(FriendlyReminderException.class)
                .hasMessageContaining("Don't panic! I'm just a friendly reminder!");
    }

    @Test
    public void should_throw_exception_when_duration_is_negative_for_after_method() {
        assertThatThrownBy(
                        () -> {
                            Mockito.after(-1);
                        })
                .isInstanceOf(FriendlyReminderException.class)
                .hasMessageContaining("Don't panic! I'm just a friendly reminder!");
    }
}
