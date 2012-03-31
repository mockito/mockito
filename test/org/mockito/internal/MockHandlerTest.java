/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.InvocationImpl;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.ArgumentMatcherStorage;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.MethodInvocationReport;
import org.mockitoutil.TestBase;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@SuppressWarnings({ "unchecked", "serial" })
public class MockHandlerTest extends TestBase {

	private StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
	private InvocationImpl invocation = mock(InvocationImpl.class);


	@Test
	public void shouldRemoveVerificationModeEvenWhenInvalidMatchers() throws Throwable {
		// given
		InvocationImpl invocation = new InvocationBuilder().toInvocation();
		@SuppressWarnings("rawtypes")
		MockHandler<?> handler = new MockHandler();
		handler.mockingProgress.verificationStarted(VerificationModeFactory.atLeastOnce());
		handler.matchersBinder = new MatchersBinder() {
			public InvocationMatcher bindMatchers(ArgumentMatcherStorage argumentMatcherStorage, InvocationImpl invocation) {
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
		MockHandler<?> handler = createCorrectlyStubbedHandler(throwingListener);

		// when
		handler.handle(invocation);
	}



	private MockHandler<?> createCorrectlyStubbedHandler(InvocationListener throwingListener) {
		MockHandler<?> handler = createHandlerWithListeners(throwingListener);
		stubOrdinaryInvocationWithGivenReturnValue(handler);
		return handler;
	}

	private void stubOrdinaryInvocationWithGivenReturnValue(MockHandler<?> handler) {
		stubOrdinaryInvocationWithInvocationMatcher(handler, stubbedInvocationMatcher);
	}



	private void stubOrdinaryInvocationWithInvocationMatcher(MockHandler<?> handler, StubbedInvocationMatcher value) {
		handler.invocationContainerImpl = mock(InvocationContainerImpl.class);
		given(handler.invocationContainerImpl.findAnswerFor(any(InvocationImpl.class))).willReturn(value);
	}




	private MockHandler<?> createHandlerWithListeners(InvocationListener... listener) {
		@SuppressWarnings("rawtypes")
		MockHandler<?> handler = new MockHandler(mock(MockSettingsImpl.class));
		handler.matchersBinder = mock(MatchersBinder.class);
		given(handler.getMockSettings().getInvocationListeners()).willReturn(Arrays.asList(listener));
		return handler;
	}
}
