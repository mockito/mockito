package org.mockitousage.debugging;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.exceptions.misusing.NullInsteadOfMockException;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.listeners.VerificationListener;
import org.mockito.verification.VerificationMode;
import org.mockito.verification.VerificationSucceededEvent;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class VerificationListenerCallBackTest {
    public static final String invocationWanted = "doSomething";

    @Test
    public void should_call_single_listener_on_verify() {
        //given
        RememberingListener listener = new RememberingListener();
        ThreadSafeMockingProgress.mockingProgress().addListener(listener);
        Foo foo = mock(Foo.class);

        //when
        VerificationMode never = never();
        verify(foo, never).doSomething("");

        //then
        assertThatHasBeenNotified(listener, foo, never, invocationWanted);
    }

    @Test
    public void should_call_all_listeners_on_verify() {
        //given
        RememberingListener listener1 = new RememberingListener();
        RememberingListener listener2 = new RememberingListener();

        MockingProgress mockingProgress = ThreadSafeMockingProgress.mockingProgress();
        mockingProgress.addListener(listener1);
        mockingProgress.addListener(listener2);

        Foo foo = mock(Foo.class);

        //when
        VerificationMode never = never();
        verify(foo, never).doSomething("");

        //then
        assertThatHasBeenNotified(listener1, foo, never, invocationWanted);
        assertThatHasBeenNotified(listener2, foo, never, invocationWanted);
    }

    @Test
    public void should_not_call_listener_when_verify_throws() {
        //given
        RememberingListener listener = new RememberingListener();
        MockingProgress mockingProgress = ThreadSafeMockingProgress.mockingProgress();

        mockingProgress.addListener(listener);

        //when
        Foo foo = null;
        try {
            verify(foo).doSomething("");
            fail("Exception expected.");
        } catch (Exception e) {
            //then
            assertThat(listener.cause).isInstanceOf(NullInsteadOfMockException.class);
        }
    }

    @Test
    public void wrong_verify_type() {
        //given
        RememberingListener listener = new RememberingListener();
        MockingProgress mockingProgress = ThreadSafeMockingProgress.mockingProgress();

        mockingProgress.addListener(listener);

        Foo foo = mock(Foo.class);
        //when
        try {
            verify(foo).doSomething("");
            fail("Exception expected.");
        } catch (Throwable e) {
            //then
            assertThat(listener.cause).isInstanceOf(MockitoAssertionError.class);
        }
    }

    private void assertThatHasBeenNotified(RememberingListener listener, Object mock, VerificationMode mode, String invocationWanted) {
        assertThat(listener.mock).isEqualTo(mock);
        assertThat(listener.mode).isEqualTo(mode);
        assertThat(listener.data.getWanted().getMethod().getName()).isEqualTo(invocationWanted);
    }


    private static class RememberingListener implements VerificationListener {
        Object mock;
        VerificationMode mode;
        VerificationData data;
        Throwable cause;

        @Override
        public void onVerificationSucceeded(VerificationSucceededEvent verificationSucceededEvent) {
            this.mock = verificationSucceededEvent.getMock();
            this.mode = verificationSucceededEvent.getMode();
            this.data = verificationSucceededEvent.getData();
        }

        @Override
        public void onVerificationException(Object mock, VerificationMode actualMode, Throwable failure) {
            this.mock = mock;
            this.mode = actualMode;
            this.cause = failure;
        }
    }
}
