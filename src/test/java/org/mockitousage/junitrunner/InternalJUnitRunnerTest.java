package org.mockitousage.junitrunner;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.exceptions.verification.TooLittleActualInvocations;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockitoutil.TestBase;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by sfaber on 4/22/16.
 */
public class InternalJUnitRunnerTest extends TestBase {

    JUnitCore runner = new JUnitCore();

    @Test public void passing_test() {
        //when
        Result result = runner.run(
                SomeFeature.class
        );
        //then
        assertThat(result).isSuccessful();
    }

    @Test public void failing_test() {
        //when
        Result result = runner.run(
                SomeFailingFeature.class
        );
        //then
        assertThat(result).fails(1, TooLittleActualInvocations.class);
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class SomeFeature {
        @Mock List list;
        @Test public void some_behavior() {
            when(list.get(0)).thenReturn("0");
            assertEquals("0", list.get(0));
        }
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class SomeFailingFeature {
        @Mock List list;
        @Test public void some_failing_behavior() {
            list.clear();
            verify(list, times(2)).clear();
        }
    }
}
