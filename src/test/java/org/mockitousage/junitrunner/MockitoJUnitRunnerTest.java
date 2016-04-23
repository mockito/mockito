package org.mockitousage.junitrunner;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockitousage.IMethods;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by sfaber on 4/22/16.
 */
public class MockitoJUnitRunnerTest {

    @Test public void succeeds_when_all_stubs_were_used() {
        JUnitCore runner = new JUnitCore();
        //when
        Result result = runner.run(
                StubbingInConstructorUsed.class,
                StubbingInBeforeUsed.class,
                StubbingInTestUsed.class
        );
        //then
        assertTrue(result.wasSuccessful());
    }

    @Ignore
    @Test public void fails_when_stubs_were_not_used() {
        JUnitCore runner = new JUnitCore();
        Class[] tests = {StubbingInConstructorUnused.class,
                StubbingInBeforeUnused.class,
                StubbingInTestUnused.class};

        //when
        Result result = runner.run(tests);
        System.out.println(result.getFailures());
        //then
        assertEquals(tests.length, result.getFailureCount());
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class StubbingInConstructorUsed extends StubbingInConstructorUnused{
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
    public static class StubbingInBeforeUsed extends StubbingInBeforeUnused{
        @Test public void test() {
            assertEquals("1", mock.simpleMethod(1));
        }
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class StubbingInBeforeUnused {
        IMethods mock;
        @Before public void before() {
            mock = when(mock(IMethods.class).simpleMethod(1)).thenReturn("1").getMock();
        }
        @Test public void dummy() {}
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class StubbingInTestUsed {
        @Test public void test() {
            IMethods mock = when(mock(IMethods.class).simpleMethod(1)).thenReturn("1").getMock();
            assertEquals("1", mock.simpleMethod(1));
        }
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class StubbingInTestUnused {
        @Test public void test() {
            when(mock(IMethods.class).simpleMethod(1)).thenReturn("1");
        }
    }
}
