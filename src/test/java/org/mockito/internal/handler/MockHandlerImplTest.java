/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.handler;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.exceptions.misusing.WrongTypeOfReturnValue;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationImpl;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.ArgumentMatcherStorage;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.invocation.Invocation;
import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.MethodInvocationReport;
import org.mockitoutil.TestBase;

import java.util.Arrays;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@SuppressWarnings({"unchecked", "serial"})
public class MockHandlerImplTest extends TestBase {

    private StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
    private Invocation invocation = mock(InvocationImpl.class);


    @Test
    public void should_remove_verification_mode_even_when_invalid_matchers() throws Throwable {
        // given
        Invocation invocation = new InvocationBuilder().toInvocation();
        @SuppressWarnings("rawtypes")
        MockHandlerImpl<?> handler = new MockHandlerImpl(new MockSettingsImpl());
        mockingProgress().verificationStarted(VerificationModeFactory.atLeastOnce());
        handler.matchersBinder = new MatchersBinder() {
            public InvocationMatcher bindMatchers(ArgumentMatcherStorage argumentMatcherStorage, Invocation invocation) {
                throw new InvalidUseOfMatchersException();
            }
        };

        try {
            // when
            handler.handle(invocation);

            // then
            fail();
        } catch (InvalidUseOfMatchersException ignored) {
        }

        assertNull(mockingProgress().pullVerificationMode());
    }


    @Test(expected = MockitoException.class)
    public void should_throw_mockito_exception_when_invocation_handler_throws_anything() throws Throwable {
        // given
        InvocationListener throwingListener = mock(InvocationListener.class);
        doThrow(new Throwable()).when(throwingListener).reportInvocation(any(MethodInvocationReport.class));
        MockHandlerImpl<?> handler = create_correctly_stubbed_handler(throwingListener);

        // when
        handler.handle(invocation);
    }

    @Test(expected = WrongTypeOfReturnValue.class)
    public void should_report_bogus_default_answer() throws Throwable {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandlerImpl<?> handler = new MockHandlerImpl(mockSettings);
        given(mockSettings.getDefaultAnswer()).willReturn(new Returns(AWrongType.WRONG_TYPE));

        @SuppressWarnings("unused") // otherwise cast is not done
        String there_should_not_be_a_CCE_here = (String) handler.handle(
                new InvocationBuilder().method(Object.class.getDeclaredMethod("toString")).toInvocation()
        );
    }

    private MockHandlerImpl<?> create_correctly_stubbed_handler(InvocationListener throwingListener) {
        MockHandlerImpl<?> handler = create_handler_with_listeners(throwingListener);
        stub_ordinary_invocation_with_given_return_value(handler);
        return handler;
    }

    private void stub_ordinary_invocation_with_given_return_value(MockHandlerImpl<?> handler) {
        stub_ordinary_invocation_with_invocation_matcher(handler, stubbedInvocationMatcher);
    }


    private void stub_ordinary_invocation_with_invocation_matcher(MockHandlerImpl<?> handler, StubbedInvocationMatcher value) {
        handler.invocationContainerImpl = mock(InvocationContainerImpl.class);
        given(handler.invocationContainerImpl.findAnswerFor(any(InvocationImpl.class))).willReturn(value);
    }


    private MockHandlerImpl<?> create_handler_with_listeners(InvocationListener... listener) {
        @SuppressWarnings("rawtypes")
        MockHandlerImpl<?> handler = new MockHandlerImpl(mock(MockSettingsImpl.class));
        handler.matchersBinder = mock(MatchersBinder.class);
        given(handler.getMockSettings().getInvocationListeners()).willReturn(Arrays.asList(listener));
        return handler;
    }

    private static class AWrongType {
        public static final AWrongType WRONG_TYPE = new AWrongType();
    }
}
