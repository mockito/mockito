package org.mockito.internal.verification;

import org.junit.Test;
import org.mockitoutil.TestBase;

import static org.hamcrest.CoreMatchers.is;

public class TimerTest extends TestBase {

    @Test
    public void should_return_true_if_task_is_in_acceptable_time_bounds() {
        long duration = 10000L;
        long now = System.currentTimeMillis();
        Timer timer = new Timer(duration);

        boolean isTimeUp = timer.isUp(now);

        assertThat(isTimeUp, is(true));
    }

    @Test
    public void should_return_false_if_task_is_outside_the_acceptable_time_bounds() {
        long duration = 0L;
        Timer timer = new Timer(duration);
        long timeFromPast = generate_time_from_past();

        boolean isTimeUp = timer.isUp(timeFromPast);

        assertThat(isTimeUp, is(false));
    }

    long generate_time_from_past() {
        return System.currentTimeMillis() - 10000L;
    }

}
