/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.handler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.listeners.NotifiedMethodInvocationReport;
import org.mockito.invocation.Invocation;
import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.MethodInvocationReport;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.mockitousage.IMethods;

import java.text.ParseException;
import java.util.ArrayList;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class InvocationNotifierHandlerTest {
    private static final String SOME_LOCATION = "some location";
    private static final RuntimeException SOME_EXCEPTION = new RuntimeException();
    private static final OutOfMemoryError SOME_ERROR = new OutOfMemoryError();
    private static final Answer SOME_ANSWER = mock(Answer.class);


    @Mock private InvocationListener listener1;
    @Mock private InvocationListener listener2;
    @Spy private CustomListener customListener;

    @Mock private Invocation invocation;
    @Mock private MockHandlerImpl mockHandler;

    private InvocationNotifierHandler notifier;

    @Before
    public void setUp() throws Exception {
        notifier = new InvocationNotifierHandler(
                mockHandler,
                (MockSettingsImpl) new MockSettingsImpl().invocationListeners(customListener, listener1, listener2)
        );
    }

    @Test
    public void should_notify_all_listeners_when_calling_delegate_handler() throws Throwable {
        // given
        given(mockHandler.handle(invocation)).willReturn("returned value");

        // when
        notifier.handle(invocation);

        // then
        verify(listener1).reportInvocation(new NotifiedMethodInvocationReport(invocation, "returned value"));
        verify(listener2).reportInvocation(new NotifiedMethodInvocationReport(invocation, "returned value"));
    }

    @Test
    public void should_notify_all_listeners_when_called_delegate_handler_returns_ex() throws Throwable {
        // given
        Exception computedException = new Exception("computed");
        given(mockHandler.handle(invocation)).willReturn(computedException);

        // when
        notifier.handle(invocation);

        // then
        verify(listener1).reportInvocation(new NotifiedMethodInvocationReport(invocation, (Object) computedException));
        verify(listener2).reportInvocation(new NotifiedMethodInvocationReport(invocation, (Object) computedException));
    }

    @Test(expected = ParseException.class)
    public void should_notify_all_listeners_when_called_delegate_handler_throws_exception_and_rethrow_it() throws Throwable {
        // given
        ParseException parseException = new ParseException("", 0);
        given(mockHandler.handle(invocation)).willThrow(parseException);

        // when
        try {
            notifier.handle(invocation);
            fail();
        } finally {
            // then
            verify(listener1).reportInvocation(new NotifiedMethodInvocationReport(invocation, parseException));
            verify(listener2).reportInvocation(new NotifiedMethodInvocationReport(invocation, parseException));
        }
    }

    @Test
    public void should_report_listener_exception() throws Throwable {
        willThrow(new NullPointerException()).given(customListener).reportInvocation(any(MethodInvocationReport.class));

        try {
            notifier.handle(invocation);
            fail();
        } catch (MockitoException me) {
            assertThat(me.getMessage())
                    .contains("invocation listener")
                    .contains("CustomListener")
                    .contains("threw an exception")
                    .contains("NullPointerException");
        }
    }

    @Test
    public void should_delegate_all_MockHandlerInterface_to_the_parameterized_MockHandler() throws Exception {
        notifier.getInvocationContainer();
        notifier.getMockSettings();
        notifier.voidMethodStubbable(mock(IMethods.class));
        notifier.setAnswersForStubbing(new ArrayList<Answer>());

        verify(mockHandler).getInvocationContainer();
        verify(mockHandler).getMockSettings();
        verify(mockHandler).voidMethodStubbable(any());
        verify(mockHandler).setAnswersForStubbing(anyList());
    }

    private static class CustomListener implements InvocationListener {
        public void reportInvocation(MethodInvocationReport methodInvocationReport) {
            // nop
        }
    }
}
