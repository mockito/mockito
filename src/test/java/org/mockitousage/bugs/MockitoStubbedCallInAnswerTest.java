/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @see <a href="https://github.com/mockito/mockito/issues/1279">Issue #1279</a>
 */
public class MockitoStubbedCallInAnswerTest extends TestBase {

    @Mock Foo foo;
    @Mock Bar bar;

    @Test
    public void stubbing_the_right_mock() throws Exception {
        //stubbing on different mock should not be altered
        when(bar.doInt()).thenReturn(0);
        when(foo.doInt()).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return bar.doInt();
            }
        });
        assertEquals(0, foo.doInt());
        assertEquals(0, bar.doInt());

        //when we override the stubbing
        when(foo.doInt()).thenReturn(1);

        //we expect it to be reflected:
        assertEquals(1, foo.doInt());

        //but the stubbing on a different mock should not be altered:
        assertEquals(0, bar.doInt());
    }

    @Test
    public void return_type_validation() throws Exception {
        when(foo.doString()).thenAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                //invoking a method on a different mock, with different return type
                return String.valueOf(bar.doInt());
            }
        });
        assertEquals("0", foo.doString());

        //we can override stubbing without misleading return type validation errors:
        when(foo.doString()).thenReturn("");
        assertEquals("", foo.doString());
    }

    @Test
    public void prevents_stack_overflow() throws Exception {
        when(foo.doInt()).thenAnswer(new Answer<Integer>() {
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return bar.doInt();
            }
        });
        assertEquals(0, foo.doInt());

        when(foo.doInt()).thenAnswer(new Answer<Integer>() {
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return bar.doInt() + 1;
            }
        });

        //calling below used to cause SO error
        assertEquals(1, foo.doInt());
    }

    @Test
    public void overriding_stubbing() throws Exception {
        when(bar.doInt()).thenReturn(10);
        when(foo.doInt()).thenAnswer(new Answer<Integer>() {
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return bar.doInt() + 1;
            }
        });

        assertEquals(11, foo.doInt());

        //when we override the stubbing with a different one
        when(foo.doInt()).thenReturn(100);

        //we expect it to be reflected:
        assertEquals(100, foo.doInt());
    }

    interface Foo {
        String doString();
        int doInt();
    }

    interface Bar {
        int doInt();
    }
}
