/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.debugging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.assertj.core.api.Condition;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.mockito.Mockito;
import org.mockito.MockitoFramework;
import org.mockito.StateMaster;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.listeners.VerificationListener;
import org.mockito.verification.VerificationEvent;
import org.mockito.verification.VerificationMode;
import org.mockitoutil.TestBase;

public class VerificationListenerCallBackTest extends TestBase {

    @After
    public void clearListeners() {
        new StateMaster().clearMockitoListeners();
    }

    @Test
    public void should_call_single_listener_on_verify() throws NoSuchMethodException {
        // given
        RememberingListener listener = new RememberingListener();
        MockitoFramework mockitoFramework = Mockito.framework();
        mockitoFramework.addListener(listener);

        Method invocationWanted = Foo.class.getDeclaredMethod("doSomething", String.class);
        Foo foo = mock(Foo.class);

        // when
        VerificationMode never = never();
        verify(foo, never).doSomething("");

        // then
        assertThat(listener).is(notifiedFor(foo, never, invocationWanted));
    }

    @Test
    public void should_call_all_listeners_on_verify() throws NoSuchMethodException {
        // given
        RememberingListener listener1 = new RememberingListener();
        RememberingListener2 listener2 = new RememberingListener2();
        Mockito.framework().addListener(listener1).addListener(listener2);

        Method invocationWanted = Foo.class.getDeclaredMethod("doSomething", String.class);
        Foo foo = mock(Foo.class);

        // when
        VerificationMode never = never();
        verify(foo, never).doSomething("");

        // then
        assertThat(listener1).is(notifiedFor(foo, never, invocationWanted));
        assertThat(listener2).is(notifiedFor(foo, never, invocationWanted));
    }

    @Test
    public void should_not_call_listener_when_verify_was_called_incorrectly() {
        // when
        VerificationListener listener = mock(VerificationListener.class);
        framework().addListener(listener);
        Foo foo = null;

        try {
            verify(foo).doSomething("");
            fail("Exception expected.");
        } catch (Exception e) {
            // then
            verify(listener, never()).onVerification(any(VerificationEvent.class));
        }
    }

    @Test
    public void should_notify_when_verification_throws_type_error() {
        // given
        RememberingListener listener = new RememberingListener();
        MockitoFramework mockitoFramework = Mockito.framework();
        mockitoFramework.addListener(listener);
        Foo foo = mock(Foo.class);

        // when
        try {
            verify(foo).doSomething("");
            fail("Exception expected.");
        } catch (Throwable e) {
            // then
            assertThat(listener.cause).isInstanceOf(MockitoAssertionError.class);
        }
    }

    @Test
    public void should_notify_when_verification_throws_runtime_exception() {
        // given
        RememberingListener listener = new RememberingListener();
        MockitoFramework mockitoFramework = Mockito.framework();
        mockitoFramework.addListener(listener);
        Foo foo = mock(Foo.class);

        // when
        try {
            verify(foo, new RuntimeExceptionVerificationMode()).doSomething("");
            fail("Exception expected.");
        } catch (Throwable e) {
            // then
            assertThat(listener.cause).isInstanceOf(RuntimeException.class);
        }
    }

    @Test
    public void should_call_verification_listeners() {
        // given
        RememberingListener listener = new RememberingListener();
        MockitoFramework mockitoFramework = Mockito.framework();
        mockitoFramework.addListener(listener);
        JUnitCore runner = new JUnitCore();

        // when
        runner.run(VerificationListenerSample.class);

        // then
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
            this.cause = verificationEvent.getVerificationError();
        }
    }

    private static class RememberingListener2 extends RememberingListener {}

    private static Condition<RememberingListener> notifiedFor(
            final Object mock, final VerificationMode mode, final Method wantedMethod) {
        return new Condition<RememberingListener>() {
            public boolean matches(RememberingListener listener) {
                assertThat(listener.mock).isEqualTo(mock);
                assertThat(listener.mode).isEqualTo(mode);
                assertThat(listener.data.getTarget().getInvocation().getMethod())
                        .isEqualTo(wantedMethod);

                return true;
            }
        };
    }

    private static class RuntimeExceptionVerificationMode implements VerificationMode {
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
