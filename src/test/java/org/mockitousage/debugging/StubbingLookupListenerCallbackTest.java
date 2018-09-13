/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.debugging;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;
import org.mockito.invocation.Invocation;
import org.mockito.listeners.StubbingLookupEvent;
import org.mockito.listeners.StubbingLookupListener;
import org.mockito.mock.MockCreationSettings;
import org.mockito.stubbing.Stubbing;
import org.mockitoutil.TestBase;

import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StubbingLookupListenerCallbackTest extends TestBase {

    @Test
    public void should_call_listener_when_mock_return_normally_with_stubbed_answer() {
        // given
        final AnswerListener listener = spy(AnswerListener.class);
        Foo foo = mock(Foo.class, withSettings().stubbingLookupListeners(listener));
        doReturn("coke").when(foo).giveMeSomeString("soda");
        doReturn("java").when(foo).giveMeSomeString("coffee");

        // when
        foo.giveMeSomeString("soda");

        // then
        verify(listener).onStubbingLookup(argThat(new ArgumentMatcher<StubbingLookupEvent>() {
            @Override
            public boolean matches(StubbingLookupEvent argument) {
                assertEquals("soda", argument.getInvocation().getArgument(0));
                assertEquals("foo", argument.getMockSettings().getMockName().toString());
                assertEquals(2, argument.getAllStubbings().size());
                assertNotNull(argument.getStubbingFound());
                return true;
            }
        }));
    }

    @Test
    public void should_call_listener_when_mock_return_normally_with_default_answer() {
        // given
        AnswerListener listener = spy(AnswerListener.class);
        Foo foo = mock(Foo.class, withSettings().stubbingLookupListeners(listener));
        doReturn("java").when(foo).giveMeSomeString("coffee");

        // when
        foo.giveMeSomeString("soda");

        // then
        verify(listener).onStubbingLookup(argThat(new ArgumentMatcher<StubbingLookupEvent>() {
            @Override
            public boolean matches(StubbingLookupEvent argument) {
                assertEquals("soda", argument.getInvocation().getArgument(0));
                assertEquals("foo", argument.getMockSettings().getMockName().toString());
                assertEquals(1, argument.getAllStubbings().size());
                assertNull(argument.getStubbingFound());
                return true;
            }
        }));
    }

    @Test
    public void should_not_call_listener_when_mock_is_not_called() {
        // given
        AnswerListener listener = spy(AnswerListener.class);
        Foo foo = mock(Foo.class, withSettings().stubbingLookupListeners(listener));
        doReturn("java").when(foo).giveMeSomeString("coffee");

        // when nothing

        // then
        verifyZeroInteractions(listener);
    }

    @Test
    public void should_allow_same_listener() {
        // given
        AnswerListener listener = mock(AnswerListener.class);
        Foo foo = mock(Foo.class, withSettings().stubbingLookupListeners(listener, listener));

        // when
        foo.giveMeSomeString("tea");
        foo.giveMeSomeString("coke");

        // then each listener was notified 2 times (notified 4 times in total)
        verify(listener, times(4)).onStubbingLookup(any(StubbingLookupEvent.class));
    }

    @Test
    public void should_call_all_listeners_in_order() {
        // given
        AnswerListener listener1 = mock(AnswerListener.class);
        AnswerListener listener2 = mock(AnswerListener.class);
        Foo foo = mock(Foo.class, withSettings().stubbingLookupListeners(listener1, listener2));
        doReturn("sprite").when(foo).giveMeSomeString("soda");

        // when
        foo.giveMeSomeString("soda");

        // then
        InOrder inOrder = inOrder(listener1, listener2);
        inOrder.verify(listener1).onStubbingLookup(any(StubbingLookupEvent.class));
        inOrder.verify(listener2).onStubbingLookup(any(StubbingLookupEvent.class));
    }

    @Test
    public void should_call_all_listeners_when_mock_throws_exception() {
        // given
        AnswerListener listener1 = mock(AnswerListener.class);
        AnswerListener listener2 = mock(AnswerListener.class);
        Foo foo = mock(Foo.class, withSettings().stubbingLookupListeners(listener1, listener2));
        doThrow(new NoWater()).when(foo).giveMeSomeString("tea");

        // when
        try {
            foo.giveMeSomeString("tea");
            fail();
        } catch (NoWater e) {
            // then
            verify(listener1).onStubbingLookup(any(StubbingLookupEvent.class));
            verify(listener2).onStubbingLookup(any(StubbingLookupEvent.class));
        }
    }

    //TODO x deleted listener

    private static class AnswerListener implements StubbingLookupListener {
        private Invocation invocation;
        private Stubbing stubbing;
        private Collection<Stubbing> allStubbings;
        private MockCreationSettings mockSettings;

        public AnswerListener() {}

        public void onStubbingLookup(StubbingLookupEvent event) {
            this.invocation = event.getInvocation();
            this.stubbing = event.getStubbingFound();
            this.allStubbings = event.getAllStubbings();
            this.mockSettings = event.getMockSettings();
        }
    }

    private static class NoWater extends RuntimeException {}
}
