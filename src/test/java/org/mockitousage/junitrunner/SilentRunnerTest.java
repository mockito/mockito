/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.junitrunner;

import java.util.List;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.UnfinishedStubbingException;
import org.mockito.exceptions.verification.TooLittleActualInvocations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockitousage.IMethods;
import org.mockitoutil.JUnitResultAssert;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SilentRunnerTest extends TestBase {

    JUnitCore runner = new JUnitCore();

    @Test public void passing_test() {
        //when
        Result result = runner.run(
                SomeFeature.class
        );
        //then
        JUnitResultAssert.assertThat(result).isSuccessful();
    }

    @Test public void failing_test() {
        //when
        Result result = runner.run(
                SomeFailingFeature.class
        );
        //then
        JUnitResultAssert.assertThat(result).fails(1, TooLittleActualInvocations.class);
    }

    @Test public void failing_test_in_constructor() {
        //when
        Result result = runner.run(
                FailsInConstructor.class
        );
        //then
        JUnitResultAssert.assertThat(result).fails(1, IllegalArgumentException.class);
    }

    @Test public void validates_framework_usage() {
        //when
        Result result = runner.run(
                UsesFrameworkIncorrectly.class
        );
        //then
        JUnitResultAssert.assertThat(result).fails(1, "unfinished_stubbing_test_method", UnfinishedStubbingException.class);
    }

    @Test
    public void ignores_unused_stubs() {
        JUnitCore runner = new JUnitCore();
        //when
        Result result = runner.run(HasUnnecessaryStubs.class);
        //then
        JUnitResultAssert.assertThat(result).isSuccessful();
    }

    @RunWith(MockitoJUnitRunner.Silent.class)
    public static class SomeFeature {
        @Mock List<String> list;
        @Test public void some_behavior() {
            when(list.get(0)).thenReturn("0");
            assertEquals("0", list.get(0));
        }
    }

    @RunWith(MockitoJUnitRunner.Silent.class)
    public static class SomeFailingFeature {
        @Mock List<String> list;
        @Test public void some_failing_behavior() {
            list.clear();
            verify(list, times(2)).clear();
        }
    }

    @RunWith(MockitoJUnitRunner.Silent.class)
    public static class FailsInConstructor {
        {
            if (System.currentTimeMillis() > 0) {
                throw new IllegalArgumentException("Boo!");
            }
        }
        @Mock List<String> list;
        @Test public void some_behavior() {}
    }

    @RunWith(MockitoJUnitRunner.Silent.class)
    public static class UsesFrameworkIncorrectly {
        @Mock List<?> list;

        @SuppressWarnings({"MockitoUsage", "CheckReturnValue"})
        @Test public void unfinished_stubbing_test_method() {
            when(list.get(0)); //unfinished stubbing
        }
    }

    /**
     * The test class itself is passing but it has some unnecessary stubs
     */
    @RunWith(MockitoJUnitRunner.Silent.class)
    public static class HasUnnecessaryStubs {
        IMethods mock1 = when(mock(IMethods.class).simpleMethod(1)).thenReturn("1").getMock();
        IMethods mock2 = when(mock(IMethods.class).simpleMethod(2)).thenReturn("2").getMock();
        IMethods mock3 = when(mock(IMethods.class).simpleMethod(3)).thenReturn("3").getMock();

        @Test
        public void usesStub() {
            assertEquals("1", mock1.simpleMethod(1));
        }

        @Test
        public void usesStubWithDifferentArg() {
            assertEquals(null, mock2.simpleMethod(200));
            assertEquals(null, mock3.simpleMethod(300));
        }
    }
}
