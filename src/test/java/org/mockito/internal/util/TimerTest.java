/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.exceptions.misusing.FriendlyReminderException;
import org.mockitoutil.TestBase;

public class TimerTest extends TestBase {

    @Test
    public void should_return_true_if_task_is_in_acceptable_time_bounds() {
        // given
        long duration = 10000L;
        Timer timer = new Timer(duration);

        // when
        timer.start();

        // then
        Assertions.assertThat(timer.isCounting()).isTrue();
    }

    @Test
    public void should_return_false_when_time_run_out() throws Exception {
        // given
        Timer timer = new Timer(0);
        timer.start();

        // when
        oneMillisecondPasses();

        // then
        Assertions.assertThat(timer.isCounting()).isFalse();
    }

    @Test
    public void should_throw_friendly_reminder_exception_when_duration_is_negative() {
        assertThatThrownBy(
                        () -> {
                            new Timer(-1);
                        })
                .isInstanceOf(FriendlyReminderException.class)
                .hasMessageContaining("Don't panic! I'm just a friendly reminder!");
    }

    private void oneMillisecondPasses() throws InterruptedException {
        Thread.sleep(1);
    }
}
