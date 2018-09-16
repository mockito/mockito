/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.debugging;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;
import org.mockito.listeners.StubbingLookupEvent;
import org.mockito.listeners.StubbingLookupListener;
import org.mockitoutil.TestBase;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StubbingLookupListenerCallbackTest extends TestBase {

    final StubbingLookupListener listener = mock(StubbingLookupListener.class);
    Foo mock = mock(Foo.class, withSettings().stubbingLookupListeners(listener));

    @Test
    public void should_call_listener_when_mock_return_normally_with_stubbed_answer() {
        // given
        doReturn("coke").when(mock).giveMeSomeString("soda");
        doReturn("java").when(mock).giveMeSomeString("coffee");

        // when
        mock.giveMeSomeString("soda");

        // then
        verify(listener).onStubbingLookup(argThat(new ArgumentMatcher<StubbingLookupEvent>() {
            @Override
            public boolean matches(StubbingLookupEvent argument) {
                assertEquals("soda", argument.getInvocation().getArgument(0));
                assertEquals("mock", argument.getMockSettings().getMockName().toString());
                assertEquals(2, argument.getAllStubbings().size());
                assertNotNull(argument.getStubbingFound());
                return true;
            }
        }));
    }

    @Test
    public void should_call_listener_when_mock_return_normally_with_default_answer() {
        // given
        doReturn("java").when(mock).giveMeSomeString("coffee");

        // when
        mock.giveMeSomeString("soda");

        // then
        verify(listener).onStubbingLookup(argThat(new ArgumentMatcher<StubbingLookupEvent>() {
            @Override
            public boolean matches(StubbingLookupEvent argument) {
                assertEquals("soda", argument.getInvocation().getArgument(0));
                assertEquals("mock", argument.getMockSettings().getMockName().toString());
                assertEquals(1, argument.getAllStubbings().size());
                assertNull(argument.getStubbingFound());
                return true;
            }
        }));
    }

    @Test
    public void should_not_call_listener_when_mock_is_not_called() {
        // when stubbing is recorded
        doReturn("java").when(mock).giveMeSomeString("coffee");

        // then
        verifyZeroInteractions(listener);
    }

    @Test
    public void should_allow_same_listener() {
        // given
        Foo mock = mock(Foo.class, withSettings().stubbingLookupListeners(listener, listener));

        // when
        mock.giveMeSomeString("tea");
        mock.giveMeSomeString("coke");

        // then each listener was notified 2 times (notified 4 times in total)
        verify(listener, times(4)).onStubbingLookup(any(StubbingLookupEvent.class));
    }

    @Test
    public void should_call_all_listeners_in_order() {
        // given
        StubbingLookupListener listener1 = mock(StubbingLookupListener.class);
        StubbingLookupListener listener2 = mock(StubbingLookupListener.class);
        Foo mock = mock(Foo.class, withSettings().stubbingLookupListeners(listener1, listener2));
        doReturn("sprite").when(mock).giveMeSomeString("soda");

        // when
        mock.giveMeSomeString("soda");

        // then
        InOrder inOrder = inOrder(listener1, listener2);
        inOrder.verify(listener1).onStubbingLookup(any(StubbingLookupEvent.class));
        inOrder.verify(listener2).onStubbingLookup(any(StubbingLookupEvent.class));
    }

    @Test
    public void should_call_all_listeners_when_mock_throws_exception() {
        // given
        StubbingLookupListener listener1 = mock(StubbingLookupListener.class);
        StubbingLookupListener listener2 = mock(StubbingLookupListener.class);
        Foo mock = mock(Foo.class, withSettings().stubbingLookupListeners(listener1, listener2));
        doThrow(new NoWater()).when(mock).giveMeSomeString("tea");

        // when
        try {
            mock.giveMeSomeString("tea");
            fail();
        } catch (NoWater e) {
            // then
            verify(listener1).onStubbingLookup(any(StubbingLookupEvent.class));
            verify(listener2).onStubbingLookup(any(StubbingLookupEvent.class));
        }
    }

    //TODO x deleted listener

    private static class NoWater extends RuntimeException {}
}
