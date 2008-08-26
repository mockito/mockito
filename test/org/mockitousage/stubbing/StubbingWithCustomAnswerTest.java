/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings({"unchecked", "deprecation"})
public class StubbingWithCustomAnswerTest extends TestBase {
    @Mock
    private IMethods mock;

    @Test
    public void shouldAnswer() throws Exception {
        stub(mock.simpleMethod(anyString())).toAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                String arg = (String) invocation.getArguments()[0];

                return invocation.getMethod().getName() + "-" + arg;
            }
        });

        assertEquals("simpleMethod-test", mock.simpleMethod("test"));
    }

    @Test
    public void shouldAnswerConsecutively() throws Exception {
        stub(mock.simpleMethod())
                .toAnswer(new Answer<String>() {
                    public String answer(InvocationOnMock invocation) throws Throwable {
                        return invocation.getMethod().getName();
                    }
                })
                .toReturn("Hello")
                .toAnswer(new Answer<String>() {
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

        stubVoid(mock).toAnswer(recordCall).on().voidMethod();

        mock.voidMethod();
        assertTrue(recordCall.isCalled());
    }

    @Test
    public void shouldAnswerVoidMethodConsecutively() throws Exception {
        RecordCall call1 = new RecordCall();
        RecordCall call2 = new RecordCall();

        stubVoid(mock).toAnswer(call1)
                .toThrow(new UnsupportedOperationException())
                .toAnswer(call2)
                .on().voidMethod();

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

    private static class RecordCall implements Answer {
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
