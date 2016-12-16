package org.mockitousage.debugging;

import java.lang.reflect.Method;
import org.assertj.core.api.Condition;
import org.junit.After;
import org.junit.Before;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.framework;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class VerificationListenerCallBackTest {
    private Method invocationWanted;
    private RememberingListener listener;
    private MockitoFramework mockitoFramework;

    @Before
    public void setUp() throws NoSuchMethodException {
        listener = new RememberingListener();
        mockitoFramework = Mockito.framework();
        mockitoFramework.addListener(listener);
        invocationWanted = Foo.class.getDeclaredMethod("doSomething", String.class);
    }

    @After
    public void reset_mockito() {
        StateMaster stateMaster = new StateMaster();
        stateMaster.reset();
        stateMaster.clearMockitoListeners();
    }

    @Test
    public void should_call_single_listener_on_verify() {
        //given
        Foo foo = mock(Foo.class);

        //when
        VerificationMode never = never();
        verify(foo, never).doSomething("");

        //then
        assertThat(listener).is(notifiedFor(foo, never, invocationWanted));
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
        assertThat(listener).is(notifiedFor(foo, never, invocationWanted));
        assertThat(listener2).is(notifiedFor(foo, never, invocationWanted));
    }

    @Test
    public void should_not_call_listener_when_verify_was_called_incorrectly() {
        //when
        Foo foo = null;
        VerificationListener mockListener = mock(VerificationListener.class);
        framework().addListener(mockListener);

        try {
            verify(foo).doSomething("");
            fail("Exception expected.");
        } catch (Exception e) {
            //then
            verify(mockListener, never()).onVerification(any(VerificationEvent.class));
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

    private void assertThatHasBeenNotified(RememberingListener listener, Object mock, VerificationMode mode, Method invocationWanted) {
        assertThat(listener.mock).isEqualTo(mock);
        assertThat(listener.mode).isEqualTo(mode);
        assertThat(listener.data.getTarget().getInvocation().getMethod()).isEqualTo(invocationWanted);
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

    private static Condition<RememberingListener> notifiedFor(final Object mock, final VerificationMode mode, final Method wantedMethod) {
        return new Condition<RememberingListener>() {
            public boolean matches(RememberingListener listener) {
                assertThat(listener.mock).isEqualTo(mock);
                assertThat(listener.mode).isEqualTo(mode);
                assertThat(listener.data.getTarget().getInvocation().getMethod()).isEqualTo(wantedMethod);

                return true;
            }
        };
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
