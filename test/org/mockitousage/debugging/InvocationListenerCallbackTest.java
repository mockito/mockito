/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.debugging;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.MethodInvocationReport;


/**
 * Ensures that custom listeners can be registered and will be called every time
 * a method on a mock is invoked.
 */
@SuppressWarnings("serial")
public class InvocationListenerCallbackTest {

    // Cannot use a mockito-mock here: during stubbing, the listener1 will be called
    // and mockito will confuse the mocks.
    private final RememberingListener listener1 = new RememberingListener();
    private final RememberingListener listener2 = new RememberingListener();

    @Test
    public void should_call_single_listener_when_mock_return_normally() throws Exception {
        // given
        final Foo foo = mock(Foo.class, withSettings().invocationListeners(listener1));
        willReturn("basil").given(foo).giveMeSomeString("herb");

        // when
        foo.giveMeSomeString("herb");

        // then
        assertThatHasBeenNotified(listener1, "basil", getClass().getSimpleName());
    }

    @Test
    public void should_call_all_listener_when_mock_return_normally() throws Exception {
        // given
        final Foo foo = mock(Foo.class, withSettings().invocationListeners(listener1, listener2));
        given(foo.giveMeSomeString("herb")).willReturn("rosemary");

        // when
        foo.giveMeSomeString("herb");

        // then
        assertThatHasBeenNotified(listener1, "rosemary", getClass().getSimpleName());
        assertThatHasBeenNotified(listener2, "rosemary", getClass().getSimpleName());
    }


    @Test
    public void should_call_all_listener_when_mock_throws_exception() throws Exception {
        // given
        final InvocationListener listener1 = mock(InvocationListener.class, "listener1");
        final InvocationListener listener2 = mock(InvocationListener.class, "listener2");
        final Foo foo = mock(Foo.class, withSettings().invocationListeners(listener1, listener2));
        doThrow(new OvenNotWorking()).when(foo).doSomething("cook");

        // when
        try {
            foo.doSomething("cook");
            fail("Exception expected.");
        } catch (final OvenNotWorking actualException) {
            // then
            final InOrder orderedVerify = inOrder(listener1, listener2);
            orderedVerify.verify(listener1).reportInvocation(any(MethodInvocationReport.class));
            orderedVerify.verify(listener2).reportInvocation(any(MethodInvocationReport.class));
        }
    }

    static class OvenNotWorking extends RuntimeException { }

    private void assertThatHasBeenNotified(final RememberingListener listener, final Object returned, final String location) {
        assertThat(listener.returnValue).isEqualTo(returned);
        assertThat(listener.invocation).isNotNull();
        assertThat(listener.locationOfStubbing).contains(getClass().getSimpleName());
    }

    private static class RememberingListener implements InvocationListener {
        DescribedInvocation invocation;
        Object returnValue;
        String locationOfStubbing;

        public void reportInvocation(final MethodInvocationReport mcr) {
            this.invocation = mcr.getInvocation();
            this.returnValue = mcr.getReturnedValue();
            this.locationOfStubbing = mcr.getLocationOfStubbing();
        }
    }
}
