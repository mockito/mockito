/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.debugging;

import org.assertj.core.api.Condition;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.MethodInvocationReport;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;


/**
 * Ensures that custom listeners can be registered and will be called every time
 * a method on a mock is invoked.
 */
public class InvocationListenerCallbackTest {

    @Test
    public void should_call_single_listener_when_mock_return_normally() throws Exception {
        // given
        RememberingListener listener = new RememberingListener();
        Foo foo = mock(Foo.class, withSettings().invocationListeners(listener));
        willReturn("basil").given(foo).giveMeSomeString("herb");

        // when
        foo.giveMeSomeString("herb");

        // then
        assertThat(listener).is(notifiedFor("basil", getClass().getSimpleName()));
    }

    @Test
    public void should_call_listeners_in_order() throws Exception {
        // given
        List<InvocationListener> container = new ArrayList<InvocationListener>();
        RememberingListener listener1 = new RememberingListener(container);
        RememberingListener listener2 = new RememberingListener(container);

        Foo foo = mock(Foo.class, withSettings().invocationListeners(listener1, listener2));

        // when
        foo.giveMeSomeString("herb");

        // then
        assertThat(container).containsExactly(listener1, listener2);
    }

    @Test
    public void should_allow_same_listener() throws Exception {
        // given
        List<InvocationListener> container = new ArrayList<InvocationListener>();
        RememberingListener listener1 = new RememberingListener(container);

        Foo foo = mock(Foo.class, withSettings().invocationListeners(listener1, listener1));

        // when
        foo.giveMeSomeString("a");
        foo.giveMeSomeString("b");

        // then each listener was notified 2 times (notified 4 times in total)
        assertThat(container).containsExactly(listener1, listener1, listener1, listener1);
    }

    @Test
    public void should_call_all_listener_when_mock_return_normally() throws Exception {
        // given
        RememberingListener listener1 = new RememberingListener();
        RememberingListener listener2 = new RememberingListener();
        Foo foo = mock(Foo.class, withSettings().invocationListeners(listener1, listener2));
        given(foo.giveMeSomeString("herb")).willReturn("rosemary");

        // when
        foo.giveMeSomeString("herb");

        // then
        assertThat(listener1).is(notifiedFor("rosemary", getClass().getSimpleName()));
        assertThat(listener2).is(notifiedFor("rosemary", getClass().getSimpleName()));
    }

    @Test
    public void should_call_all_listener_when_mock_throws_exception() throws Exception {
        // given
        InvocationListener listener1 = mock(InvocationListener.class, "listener1");
        InvocationListener listener2 = mock(InvocationListener.class, "listener2");
        Foo foo = mock(Foo.class, withSettings().invocationListeners(listener1, listener2));
        doThrow(new OvenNotWorking()).when(foo).doSomething("cook");

        // when
        try {
            foo.doSomething("cook");
            fail("Exception expected.");
        } catch (OvenNotWorking actualException) {
            // then
            InOrder orderedVerify = inOrder(listener1, listener2);
            orderedVerify.verify(listener1).reportInvocation(any(MethodInvocationReport.class));
            orderedVerify.verify(listener2).reportInvocation(any(MethodInvocationReport.class));
        }
    }

    private static Condition<RememberingListener> notifiedFor(final Object returned, final String location) {
        return new Condition<RememberingListener>() {
            public boolean matches(RememberingListener toBeAsserted) {
                assertThat(toBeAsserted.returnValue).isEqualTo(returned);
                assertThat(toBeAsserted.invocation).isNotNull();
                assertThat(toBeAsserted.locationOfStubbing).contains(location);
                return true;
            }
        };
    }

    private static class RememberingListener implements InvocationListener {
        private final List<InvocationListener> listenerContainer;
        DescribedInvocation invocation;
        Object returnValue;
        String locationOfStubbing;

        RememberingListener(List<InvocationListener> listenerContainer) {
            this.listenerContainer = listenerContainer;
        }

        RememberingListener() {
            this(new ArrayList<InvocationListener>());
        }

        public void reportInvocation(MethodInvocationReport mcr) {
            this.invocation = mcr.getInvocation();
            this.returnValue = mcr.getReturnedValue();
            this.locationOfStubbing = mcr.getLocationOfStubbing();
            listenerContainer.add(this); //so that we can assert on order
        }
    }

    private static class OvenNotWorking extends RuntimeException { }
}
