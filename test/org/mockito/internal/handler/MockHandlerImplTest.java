/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.handler;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.invocation.InvocationPhase.*;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationImpl;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.ArgumentMatcherStorage;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.InvocationPhase;
import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.MethodInvocationReport;
import org.mockito.stubbing.Answer;
import org.mockitoutil.TestBase;

import java.util.Arrays;

@SuppressWarnings({ "unchecked", "serial" })
public class MockHandlerImplTest extends TestBase {

	private StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
	private Invocation invocation = mock(InvocationImpl.class);


	@Test
	public void shouldRemoveVerificationModeEvenWhenInvalidMatchers() throws Throwable {
		// given
		Invocation invocation = new InvocationBuilder().toInvocation();
		@SuppressWarnings("rawtypes")
        MockHandlerImpl<?> handler = new MockHandlerImpl(new MockSettingsImpl());
		handler.mockingProgress.verificationStarted(VerificationModeFactory.atLeastOnce());
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
		} catch (InvalidUseOfMatchersException e) {
		}

		assertNull(handler.mockingProgress.pullVerificationMode());
	}




	@Test(expected = MockitoException.class)
	public void shouldThrowMockitoExceptionWhenInvocationHandlerThrowsAnything() throws Throwable {
		// given
		InvocationListener throwingListener = mock(InvocationListener.class);
		doThrow(new Throwable()).when(throwingListener).reportInvocation(any(MethodInvocationReport.class));
		MockHandlerImpl<?> handler = createCorrectlyStubbedHandler(throwingListener);

		// when
		handler.handle(invocation);
	}



	private MockHandlerImpl<?> createCorrectlyStubbedHandler(InvocationListener throwingListener) {
		MockHandlerImpl<?> handler = createHandlerWithListeners(throwingListener);
		stubOrdinaryInvocationWithGivenReturnValue(handler);
		return handler;
	}

	private void stubOrdinaryInvocationWithGivenReturnValue(MockHandlerImpl<?> handler) {
		stubOrdinaryInvocationWithInvocationMatcher(handler, stubbedInvocationMatcher);
	}



	private void stubOrdinaryInvocationWithInvocationMatcher(MockHandlerImpl<?> handler, StubbedInvocationMatcher value) {
		handler.invocationContainerImpl = mock(InvocationContainerImpl.class);
		given(handler.invocationContainerImpl.findAnswerFor(any(InvocationImpl.class))).willReturn(value);
	}




	private MockHandlerImpl<?> createHandlerWithListeners(InvocationListener... listener) {
		@SuppressWarnings("rawtypes")
        MockHandlerImpl<?> handler = new MockHandlerImpl(mock(MockSettingsImpl.class));
		handler.matchersBinder = mock(MatchersBinder.class);
		given(handler.getMockSettings().getInvocationListeners()).willReturn(Arrays.asList(listener));
		return handler;
	}

    @Test
    public void shouldDetectDefinePhaseOnMocksDuringWhen() throws Throwable {
        InvocationListener listener = mockInvocationListener(DEFINE);

        Runnable mock = mock(Runnable.class, withSettings().invocationListeners(listener));

        doNothing().when(mock).run();
        verify(listener, times(1)).reportInvocation((MethodInvocationReport) any());
    }

    @Test
    public void shouldDetectExecutePhaseOnMocksDuringExecute() throws Throwable {
        InvocationListener listener = mockInvocationListener(DEFINE, EXECUTE);
        Runnable mock = mock(Runnable.class, withSettings().invocationListeners(listener));

        doNothing().when(mock).run();
        mock.run();
        verify(listener, times(2)).reportInvocation((MethodInvocationReport) any());
    }

    @Test
    public void shouldDetectVerifyPhaseOnMocksDuringVerify() throws Throwable {
        InvocationListener listener = mockInvocationListener(DEFINE, EXECUTE, VERIFY);
        Runnable mock = mock(Runnable.class, withSettings().invocationListeners(listener));

        doNothing().when(mock).run();
        mock.run();
        verify(mock).run();

        verify(listener, times(3)).reportInvocation((MethodInvocationReport) any());
    }


    private InvocationListener mockInvocationListener(InvocationPhase... phases)
    {
        InvocationListener listener = mock(InvocationListener.class);
        doAnswer(new ReportInvocationAnswer(phases)).when(listener).reportInvocation(
            (MethodInvocationReport) any());
        return listener;
    }

    private static class ReportInvocationAnswer implements Answer {
        private final InvocationPhase[] expectedInvocationPhase;
        private int count = 0;

        private ReportInvocationAnswer(final InvocationPhase... expectedInvocationPhase)
        {
            this.expectedInvocationPhase = expectedInvocationPhase;
        }

        @Override
        public Object answer(final InvocationOnMock invocation) throws Throwable
        {
            MethodInvocationReport methodInvocationReport = (MethodInvocationReport) invocation.getArguments()[0];
            if (count >= expectedInvocationPhase.length) {
                fail("too many invocations");
            }
            assertEquals(expectedInvocationPhase[count++], ((Invocation)methodInvocationReport
                .getInvocation()).phase());
            return null;
        }
    }
}
