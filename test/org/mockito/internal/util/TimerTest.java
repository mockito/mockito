package org.mockito.internal.util;

import org.junit.Test;
import org.mockitoutil.TestBase;

import static org.hamcrest.CoreMatchers.is;

public class TimerTest extends TestBase {

    @Test
    public void should_return_true_if_task_is_in_acceptable_time_bounds() {
        //given
        long duration = 10000L;
        Timer timer = new Timer(duration);

        //when
        timer.start();
        boolean stillCounting = timer.isCounting();

        //then
        assertThat(stillCounting, is(true));
    }

    @Test
    public void should_return_false_if_task_is_outside_the_acceptable_time_bounds() {
        //given
        Timer timer = new Timer(-1);
        timer.start();

        //when
        boolean stillCounting = timer.isCounting();

        //then
        assertThat(timer.isCounting(), is(false));
    }
}
