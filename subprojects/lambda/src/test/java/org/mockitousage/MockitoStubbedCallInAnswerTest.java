/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoLambda.when;
import static org.mockito.MockitoLambda.mock;

/**
 * Shows how #1279 can be resolved.
 */
public class MockitoStubbedCallInAnswerTest {

    @Test
    public void direct_invocation_in_answer() throws Exception {
        Foo foo = mock(Foo.class);
        Bar bar = mock(Bar.class);

        when(foo::doInt).invokedWithNoArgs().thenAnswer(bar::doInt);
        assertEquals(0, foo.doInt());
        assertEquals(0, bar.doInt());

        when(foo::doInt).invokedWithNoArgs().thenReturn(1);
        assertEquals(1, foo.doInt());
        assertEquals(0, bar.doInt());
    }

    @Test
    public void use_other_stubbed_method_return_type_in_logic() throws Exception {
        Foo foo = mock(Foo.class);
        Bar bar = mock(Bar.class);

        when(foo::doString).invokedWithNoArgs().thenAnswer(() -> String.valueOf(bar.doInt()));
        assertEquals("0", foo.doString());

        when(foo::doString).invokedWithNoArgs().thenReturn("");
        assertEquals("", foo.doString());
    }

    @Test
    public void does_not_produce_stack_overflow_when_stubbing_twice() throws Exception {
        Foo foo = mock(Foo.class);
        Bar bar = mock(Bar.class);

        when(foo::doInt).invokedWithNoArgs().thenAnswer(bar::doInt);
        assertEquals(0, foo.doInt());

        when(foo::doInt).invokedWithNoArgs().thenAnswer(() -> bar.doInt() + 1);
        assertEquals(1, foo.doInt());
    }

    interface Foo {
        String doString();
        int doInt();
    }

    interface Bar {
        int doInt();
    }
}
