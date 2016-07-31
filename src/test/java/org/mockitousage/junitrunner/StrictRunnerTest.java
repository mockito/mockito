package org.mockitousage.junitrunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockitousage.IMethods;
import org.mockitoutil.JUnitResultAssert;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created by sfaber on 4/22/16.
 */
public class StrictRunnerTest extends TestBase {

    JUnitCore runner = new JUnitCore();

    @Test public void succeeds_when_all_stubs_were_used() {
        //when
        Result result = runner.run(
                StubbingInConstructorUsed.class,
                StubbingInBeforeUsed.class,
                StubbingInTestUsed.class
        );
        //then
        JUnitResultAssert.assertThat(result).isSuccessful();
    }

    @Test public void fails_when_stubs_were_not_used() {
        Class[] tests = {StubbingInConstructorUnused.class,
                StubbingInBeforeUnused.class,
                StubbingInTestUnused.class};

        //when
        Result result = runner.run(tests);

        //then
        JUnitResultAssert.assertThat(result).fails(3, UnnecessaryStubbingException.class);
    }

    @Test public void does_not_report_unused_stubs_when_different_failure_is_present() {
        //when
        Result result = runner.run(WithUnrelatedAssertionFailure.class);

        //then
        JUnitResultAssert.assertThat(result).fails(1, MyAssertionError.class);
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class StubbingInConstructorUsed extends StubbingInConstructorUnused {
        @Test public void test() {
            assertEquals("1", mock.simpleMethod(1));
        }
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class StubbingInConstructorUnused {
        IMethods mock = when(mock(IMethods.class).simpleMethod(1)).thenReturn("1").getMock();
        @Test public void dummy() {}
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class StubbingInBeforeUsed extends StubbingInBeforeUnused {
        @Test public void test() {
            assertEquals("1", mock.simpleMethod(1));
        }
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class StubbingInBeforeUnused {
        @Mock IMethods mock;
        @Before public void before() {
            when(mock.simpleMethod(1)).thenReturn("1");
        }
        @Test public void dummy() {}
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class StubbingInTestUsed {
        @Test public void test() {
            IMethods mock = mock(IMethods.class);
            when(mock.simpleMethod(1)).thenReturn("1");
            assertEquals("1", mock.simpleMethod(1));
        }
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class StubbingInTestUnused {
        @Test public void test() {
            IMethods mock = mock(IMethods.class);
            when(mock.simpleMethod(1)).thenReturn("1");
            mock.simpleMethod(2); //different arg
        }
    }

    private static class MyAssertionError extends AssertionError {}

    @RunWith(MockitoJUnitRunner.class)
    public static class WithUnrelatedAssertionFailure {

        IMethods mock = mock(IMethods.class);
        IMethods mock2 = mock(IMethods.class);

        @Before public void before() {
            when(mock2.simpleMethod("unused stubbing")).thenReturn("");
        }

        @Test public void passing_test() {
            when(mock.simpleMethod(1)).thenReturn("1");
            assertEquals("1", mock.simpleMethod(1));
        }

        @Test public void failing_test() {
            throw new MyAssertionError();
        }
    }
}
