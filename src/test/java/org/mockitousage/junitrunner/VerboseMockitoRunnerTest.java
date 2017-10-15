/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.junitrunner;

import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.exceptions.ExceptionIncludingMockitoWarnings;
import org.mockito.runners.VerboseMockitoJUnitRunner;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

//@RunWith(ConsoleSpammingMockitoJUnitRunner.class)
@RunWith(VerboseMockitoJUnitRunner.class)
//TODO
public class VerboseMockitoRunnerTest extends TestBase {

    @Mock private IMethods mock;

    public static class NoWarnings {

        @Test
        @Ignore
        public void test() {
            IMethods mock = mock(IMethods.class);
            mock.simpleMethod(1);
            mock.otherMethod();

            verify(mock).simpleMethod(1);
            throw new RuntimeException("boo");
        }
    }

    public static class ContainsWarnings extends TestCase {

        public ContainsWarnings() {
            super("test");
        }

        public void testIgnored() {}

        public void _test() {
            IMethods mock = mock(IMethods.class);

            //some stubbing
            when(mock.simpleMethod(1)).thenReturn("foo");
            when(mock.otherMethod()).thenReturn("foo");
            when(mock.booleanObjectReturningMethod()).thenReturn(false);

            //stub called with different args:
            String ret = mock.simpleMethod(2);

            //assertion fails due to stub called with different args
            assertEquals("foo", ret);
        }
    }

    public void cleanStackTraces() {
        makeStackTracesClean();
    }

    @Test
    @Ignore
    public void shouldContainWarnings() throws Exception {
        //when
        Result result = new JUnitCore().run(new ContainsWarnings());
        //then
        assertEquals(1, result.getFailures().size());
        Throwable exception = result.getFailures().get(0).getException();
        assertTrue(exception instanceof ExceptionIncludingMockitoWarnings);
    }

    @Test
    @Ignore
    public void shouldNotContainWarnings() throws Exception {
        Result result = new JUnitCore().run(NoWarnings.class);
        assertEquals(1, result.getFailures().size());
        assertEquals("boo", result.getFailures().get(0).getException().getMessage());
    }
}
