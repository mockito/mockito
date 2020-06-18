/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.junitrunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.mockitousage.IMethods;
import org.mockitoutil.JUnitResultAssert;
import org.mockitoutil.TestBase;

public class StrictRunnerTest extends TestBase {

    JUnitCore runner = new JUnitCore();

    @Test
    public void succeeds_when_all_stubs_were_used() {
        // when
        Result result =
                runner.run(
                        StubbingInConstructorUsed.class,
                        StubbingInBeforeUsed.class,
                        StubbingInTestUsed.class);
        // then
        JUnitResultAssert.assertThat(result).isSuccessful();
    }

    @Test
    public void fails_when_stubs_were_not_used() {
        Class[] tests = {
            StubbingInConstructorUnused.class,
            StubbingInBeforeUnused.class,
            StubbingInTestUnused.class
        };

        // when
        Result result = runner.run(tests);

        // then
        JUnitResultAssert.assertThat(result).fails(3, UnnecessaryStubbingException.class);
    }

    @Test
    public void does_not_report_unused_stubs_when_different_failure_is_present() {
        // when
        Result result = runner.run(WithUnrelatedAssertionFailure.class);

        // then
        JUnitResultAssert.assertThat(result).fails(1, MyAssertionError.class);
    }

    @Test
    public void runner_can_coexist_with_rule() {
        // I don't believe that this scenario is useful
        // I only wish that Mockito does not break awkwardly when both: runner & rule is used

        // when
        Result result = runner.run(RunnerAndRule.class);

        // then
        JUnitResultAssert.assertThat(result).fails(1, UnnecessaryStubbingException.class);
    }

    @Test
    public void runner_in_multi_threaded_tests() {
        // when
        Result result = runner.run(StubUsedFromDifferentThread.class);

        // then
        JUnitResultAssert.assertThat(result).isSuccessful();
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class StubbingInConstructorUsed extends StubbingInConstructorUnused {
        @Test
        public void test() {
            assertEquals("1", mock.simpleMethod(1));
        }
    }

    @RunWith(MockitoJUnitRunner.Strict.class) // using Strict to make sure it does the right thing
    public static class StubbingInConstructorUnused {
        IMethods mock = when(mock(IMethods.class).simpleMethod(1)).thenReturn("1").getMock();

        @Test
        public void dummy() {}
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class StubbingInBeforeUsed extends StubbingInBeforeUnused {
        @Test
        public void test() {
            assertEquals("1", mock.simpleMethod(1));
        }
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class StubbingInBeforeUnused {
        @Mock IMethods mock;

        @Before
        public void before() {
            when(mock.simpleMethod(1)).thenReturn("1");
        }

        @Test
        public void dummy() {}
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class StubbingInTestUsed {
        @Test
        public void test() {
            IMethods mock = mock(IMethods.class);
            when(mock.simpleMethod(1)).thenReturn("1");
            assertEquals("1", mock.simpleMethod(1));
        }
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class StubbingInTestUnused {
        @Test
        public void test() {
            IMethods mock = mock(IMethods.class);
            when(mock.simpleMethod(1)).thenReturn("1");
            mock.simpleMethod(2); // different arg
        }
    }

    private static class MyAssertionError extends AssertionError {}

    @RunWith(MockitoJUnitRunner.class)
    public static class WithUnrelatedAssertionFailure {

        IMethods mock = mock(IMethods.class);
        IMethods mock2 = mock(IMethods.class);

        @Before
        public void before() {
            when(mock2.simpleMethod("unused stubbing")).thenReturn("");
        }

        @Test
        public void passing_test() {
            when(mock.simpleMethod(1)).thenReturn("1");
            assertEquals("1", mock.simpleMethod(1));
        }

        @Test
        public void failing_test() {
            throw new MyAssertionError();
        }
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class RunnerAndRule {

        public @Rule MockitoRule rule = MockitoJUnit.rule();
        IMethods mock = mock(IMethods.class);

        @Test
        public void passing_test() {
            when(mock.simpleMethod(1)).thenReturn("1");
            mock.simpleMethod(2);
        }
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class StubUsedFromDifferentThread {

        IMethods mock = mock(IMethods.class);

        @Test
        public void passing_test() throws Exception {
            // stubbing is done in main thread:
            when(mock.simpleMethod(1)).thenReturn("1");

            // stubbing is used in a different thread
            // stubbing should not be reported as unused by the runner
            Thread t =
                    new Thread() {
                        public void run() {
                            mock.simpleMethod(1);
                        }
                    };
            t.start();
            t.join();
        }
    }
}
