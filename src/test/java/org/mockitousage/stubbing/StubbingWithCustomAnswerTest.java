/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.lang.reflect.Method;
import java.util.Set;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.*;

public class StubbingWithCustomAnswerTest extends TestBase {
    @Mock
    private IMethods mock;

    @Test
    public void shouldAnswer() throws Exception {
        when(mock.simpleMethod(anyString())).thenAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                String arg =  invocation.getArgument(0);

                return invocation.getMethod().getName() + "-" + arg;
            }
        });

        assertEquals("simpleMethod-test", mock.simpleMethod("test"));
    }

    @Test
    public void shouldAnswerWithThenAnswerAlias() throws Exception {
        RecordCall recordCall = new RecordCall();
        Set<?> mockedSet = (Set<?>) when(mock(Set.class).isEmpty()).then(recordCall).getMock();

        mockedSet.isEmpty();

        assertTrue(recordCall.isCalled());
    }

    @Test
    public void shouldAnswerConsecutively() throws Exception {
        when(mock.simpleMethod())
                .thenAnswer(new Answer<String>() {
                    public String answer(InvocationOnMock invocation) throws Throwable {
                        return invocation.getMethod().getName();
                    }
                })
                .thenReturn("Hello")
                .thenAnswer(new Answer<String>() {
                    public String answer(InvocationOnMock invocation) throws Throwable {
                        return invocation.getMethod().getName() + "-1";
                    }
                });

        assertEquals("simpleMethod", mock.simpleMethod());
        assertEquals("Hello", mock.simpleMethod());
        assertEquals("simpleMethod-1", mock.simpleMethod());
        assertEquals("simpleMethod-1", mock.simpleMethod());
    }

    @Test
    public void shoudAnswerVoidMethod() throws Exception {
        RecordCall recordCall = new RecordCall();

        doAnswer(recordCall).when(mock).voidMethod();

        mock.voidMethod();
        assertTrue(recordCall.isCalled());
    }

    @Test
    public void shouldAnswerVoidMethodConsecutively() throws Exception {
        RecordCall call1 = new RecordCall();
        RecordCall call2 = new RecordCall();

        doAnswer(call1)
        .doThrow(new UnsupportedOperationException())
        .doAnswer(call2)
        .when(mock).voidMethod();

        mock.voidMethod();
        assertTrue(call1.isCalled());
        assertFalse(call2.isCalled());

        try {
            mock.voidMethod();
            fail();
        } catch (UnsupportedOperationException e) {
        }

        mock.voidMethod();
        assertTrue(call2.isCalled());
    }

    @Test
    public void shouldMakeSureTheInterfaceDoesNotChange() throws Exception {
        when(mock.simpleMethod(anyString())).thenAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                assertTrue(invocation.getArguments().getClass().isArray());
                assertEquals(Method.class, invocation.getMethod().getClass());

                return "assertions passed";
            }
        });

        assertEquals("assertions passed", mock.simpleMethod("test"));
    }

    private static class RecordCall implements Answer<Object> {
        private boolean called = false;

        public boolean isCalled() {
            return called;
        }

        public Object answer(InvocationOnMock invocation) throws Throwable {
            called = true;
            return null;
        }
    }

}
