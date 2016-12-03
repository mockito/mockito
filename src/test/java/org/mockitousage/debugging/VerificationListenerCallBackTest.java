package org.mockitousage.debugging;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.mockito.Mockito;
import org.mockito.MockitoFramework;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.listeners.VerificationListener;
import org.mockito.verification.VerificationEvent;
import org.mockito.verification.VerificationMode;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class VerificationListenerCallBackTest {
    public static final String invocationWanted = "doSomething";
    private RememberingListener listener;
    private MockitoFramework mockitoFramework;

    @Before
    public void setUp() {
        listener = new RememberingListener();
        mockitoFramework = Mockito.framework();
        mockitoFramework.addListener(listener);

    }

    @Test
    public void should_call_single_listener_on_verify() {
        //given
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
        RememberingListener listener2 = new RememberingListener();
        mockitoFramework.addListener(listener2);

        Foo foo = mock(Foo.class);

        //when
        VerificationMode never = never();
        verify(foo, never).doSomething("");

        //then
        assertThatHasBeenNotified(listener, foo, never, invocationWanted);
        assertThatHasBeenNotified(listener2, foo, never, invocationWanted);
    }

    @Test
    public void should_not_call_listener_when_verify_was_called_incorrectly() {
        //when
        Foo foo = null;
        try {
            verify(foo).doSomething("");
            fail("Exception expected.");
        } catch (Exception e) {
            //then
            assertNull(listener.cause);
        }
    }

    @Test
    public void wrong_verification_type() {
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

    @Test
    public void verification_that_throws_runtime_exception() {
        Foo foo = mock(Foo.class);
        //when
        try {
            verify(foo, new RuntimeExceptionVerificationMode()).doSomething("");
            fail("Exception expected.");
        } catch (Throwable e) {
            //then
            assertThat(listener.cause).isInstanceOf(RuntimeException.class);
        }
    }

    @Test
    public void should_call_verification_listeners() {
        JUnitCore runner = new JUnitCore();
        runner.run(VerificationListenerSample.class);

        assertThat(listener.mock).isNotNull();
        assertThat(listener.mode).isEqualToComparingFieldByField(times(1));
    }

    public static class VerificationListenerSample {

        @Test
        public void verificationTest() {
            Foo foo = mock(Foo.class);
            foo.doSomething("");
            verify(foo).doSomething("");
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
        public void onVerification(VerificationEvent verificationEvent) {
            this.mock = verificationEvent.getMock();
            this.mode = verificationEvent.getMode();
            this.data = verificationEvent.getData();
            this.cause = verificationEvent.getCause();
        }
    }

    public static class RuntimeExceptionVerificationMode implements VerificationMode {
        @Override
        public void verify(VerificationData data) {
            throw new RuntimeException();
        }

        @Override
        public VerificationMode description(String description) {
            return null;
        }
    }
}
