/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.*;
import org.mockito.internal.progress.ArgumentMatcherStorage;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.invocation.InvocationListener;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;
import org.mockitoutil.TestBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings({ "unchecked", "serial" })
public class MockHandlerTest extends TestBase {

	private static final OutOfMemoryError SOME_ERROR = new OutOfMemoryError();
	private static final RuntimeException SOME_EXCEPTION = new RuntimeException();
	private static final String SOME_RETURN_VALUE = "some return value";
	private static final String SOME_LOCATION = "some location";
	@SuppressWarnings("rawtypes")
	private static final Answer SOME_ANSWER = mock(Answer.class);
	private static final StubbedInvocationMatcher SOME_INVOCATION_MATCHER = mock(StubbedInvocationMatcher.class);
	private static final Invocation SOME_INVOCATION = mock(Invocation.class);
	@Mock
	private InvocationListener listener1;
	@Mock
	private InvocationListener listener2;

	@Test
	public void shouldRemoveVerificationModeEvenWhenInvalidMatchers() throws Throwable {
		// given
		Invocation invocation = new InvocationBuilder().toInvocation();
		@SuppressWarnings("rawtypes")
		MockHandler<?> handler = new MockHandler();
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

	@Test
	public void shouldNotifyInvocationHandlerDuringStubVoid() throws Throwable {
		// given
		MockHandler<?> handler = createHandlerWithListeners(listener1, listener2);
		stubWithInvocationDuringStubVoid(handler);
		Invocation invocation = createInvocationWithStubbingLocation(SOME_LOCATION);

		handler.handle(invocation);

		// then
		verify(listener1).invokingWithReturnValue(invocation, null, SOME_LOCATION);
		verify(listener2).invokingWithReturnValue(invocation, null, SOME_LOCATION);
	}

	private Invocation createInvocationWithStubbingLocation(String stubbingLocation) {
		Invocation invocation = mock(Invocation.class);

		StubInfo stubInfo = mock(StubInfo.class);
		given(invocation.stubInfo()).willReturn(stubInfo);

		given(stubInfo.stubbedAt()).willReturn(stubbingLocation);

		return invocation;
	}

	@Test
	public void shouldNotifyInvocationHandlerDuringVerification() throws Throwable {
		// given
		MockHandler<?> handler = createHandlerWithListeners(listener1, listener2);
		stubProgressWithVerification(handler);
		Invocation invocation = createInvocationWithStubbingLocation(SOME_LOCATION);

		// when
		handler.handle(invocation);

		// then
		verify(listener1).invokingWithReturnValue(invocation, null, SOME_LOCATION);
		verify(listener2).invokingWithReturnValue(invocation, null, SOME_LOCATION);
	}

	@Test
	public void shouldNotifyInvocationHandlerDuringOrdinaryInvocationWithGivenReturnValue() throws Throwable {
		// given
		MockHandler<?> handler = createHandlerWithListeners(listener1, listener2);
		stubOrdinaryInvocationWithReturnValue(handler, SOME_RETURN_VALUE);
		Invocation invocation = createInvocationWithStubbingLocation(SOME_LOCATION);

		// when
		handler.handle(invocation);

		// then
		verify(listener1).invokingWithReturnValue(invocation, SOME_RETURN_VALUE, SOME_LOCATION);
		verify(listener2).invokingWithReturnValue(invocation, SOME_RETURN_VALUE, SOME_LOCATION);
	}

	@Test
	public void shouldNotifyInvocationHandlerDuringOrdinaryInvocationResultingInException() throws Throwable {
		// given
		MockHandler<?> handler = createHandlerWithListeners(listener1, listener2);
		stubOrdinaryInvocationWithThrowable(handler, SOME_EXCEPTION);
		Invocation invocation = createInvocationWithStubbingLocation(SOME_LOCATION);

		// when
		try {
			handler.handle(invocation);
			fail("Exception was not rethrown.");
		} catch (RuntimeException e) {
			// then
			verify(listener1).invokingWithException(invocation, SOME_EXCEPTION, SOME_LOCATION);
			verify(listener2).invokingWithException(invocation, SOME_EXCEPTION, SOME_LOCATION);
		}
	}

	@Test
	public void shouldNotNotifyInvocationHandlerDuringOrdinaryInvocationResultingInError() throws Throwable {
		// given
		MockHandler<?> handler = createHandlerWithListeners(listener1, listener2);
		stubOrdinaryInvocationWithThrowable(handler, SOME_ERROR);
		Invocation invocation = createInvocationWithStubbingLocation(SOME_LOCATION);
		
		// when
		try {
			handler.handle(invocation);
			fail("Error was not rethrown.");
		} catch (Error e) {
			// then
			verifyZeroInteractions(listener1, listener2);
		}
	}
	
	@Test
	public void shouldNotifyInvocationHandlerDuringOrdinaryInvocationWithDefaultReturnValue() throws Throwable {
		// given
		MockHandler<?> handler = createHandlerWithListeners(listener1, listener2);
		stubOrdinaryInvocationWithDefaultReturnValue(handler);
		Invocation invocation = createInvocationWithStubbingLocation(SOME_LOCATION);

		// when
		handler.handle(invocation);

		// then
		verify(listener1).invokingWithReturnValue(invocation, null, SOME_LOCATION);
		verify(listener2).invokingWithReturnValue(invocation, null, SOME_LOCATION);
	}

	@Test(expected = MockitoException.class)
	public void shouldThrowMockitoExceptionWhenInvocationHandlerThrowsAnything() throws Throwable {
		// given
		InvocationListener throwingListener = mock(InvocationListener.class);
		doThrow(SOME_EXCEPTION).when(throwingListener).invokingWithReturnValue(any(Invocation.class),
				any(String.class), any(String.class));
		MockHandler<?> handler = createCorrectlyStubbedHandler(throwingListener);

		// when
		handler.handle(SOME_INVOCATION);
	}

	private MockHandler<?> createCorrectlyStubbedHandler(InvocationListener throwingListener) {
		MockHandler<?> handler = createHandlerWithListeners(throwingListener);
		stubOrdinaryInvocationWithGivenReturnValue(handler);
		return handler;
	}

	private void stubOrdinaryInvocationWithGivenReturnValue(MockHandler<?> handler) {
		stubOrdinaryInvocationWithInvocationMatcher(handler, SOME_INVOCATION_MATCHER);
	}

	private void stubOrdinaryInvocationWithReturnValue(MockHandler<?> handler, Object returnValue) throws Throwable {
		StubbedInvocationMatcher matcher = mock(StubbedInvocationMatcher.class);
		given(matcher.answer(any(InvocationOnMock.class))).willReturn(returnValue);
		stubOrdinaryInvocationWithInvocationMatcher(handler, matcher);
	}

	private void stubOrdinaryInvocationWithThrowable(MockHandler<?> handler, Throwable throwable) throws Throwable {
		StubbedInvocationMatcher matcher = mock(StubbedInvocationMatcher.class);
		given(matcher.answer(any(InvocationOnMock.class))).willThrow(throwable);
		stubOrdinaryInvocationWithInvocationMatcher(handler, matcher);
	}

	private void stubOrdinaryInvocationWithDefaultReturnValue(MockHandler<?> handler) {
		given(handler.getMockSettings().getDefaultAnswer()).willReturn(SOME_ANSWER);
		stubOrdinaryInvocationWithInvocationMatcher(handler, null);
	}

	private void stubOrdinaryInvocationWithInvocationMatcher(MockHandler<?> handler, StubbedInvocationMatcher value) {
		handler.invocationContainerImpl = mock(InvocationContainerImpl.class);
		given(handler.invocationContainerImpl.findAnswerFor(any(Invocation.class))).willReturn(value);
	}

	private void stubProgressWithVerification(MockHandler<?> handler) {
		handler.mockingProgress = mock(MockingProgress.class);

		// Needs to be unequal to null.
		VerificationMode mode = mock(MockAwareVerificationMode.class);
		given(handler.mockingProgress.pullVerificationMode()).willReturn(mode);
	}

	@SuppressWarnings("rawtypes")
	private void stubWithInvocationDuringStubVoid(MockHandler handler) {
		stubWithPreviouslySetVoidAnswer(handler);
		stubHandlerStateWithPreviousStubVoidInvocation(handler);
	}

	private void stubHandlerStateWithPreviousStubVoidInvocation(MockHandler<?> handler) {
		StubbedInvocationMatcher invocationContainer = mock(StubbedInvocationMatcher.class);
		given(handler.matchersBinder.bindMatchers(any(ArgumentMatcherStorage.class), any(Invocation.class)))
				.willReturn(invocationContainer);

		Invocation invocation = mock(Invocation.class);
		given(invocationContainer.getInvocation()).willReturn(invocation);

		given(invocation.isVoid()).willReturn(true);
	}

	@SuppressWarnings("rawtypes")
	private void stubWithPreviouslySetVoidAnswer(MockHandler handler) {
		List<Answer> answers = new ArrayList<Answer>();
		answers.add(new DoesNothing());
		handler.setAnswersForStubbing(answers);
	}

	private MockHandler<?> createHandlerWithListeners(InvocationListener... listener) {
		@SuppressWarnings("rawtypes")
		MockHandler<?> handler = new MockHandler(mock(MockSettingsImpl.class));
		handler.matchersBinder = mock(MatchersBinder.class);
		given(handler.getMockSettings().getInvocationListeners()).willReturn(Arrays.asList(listener));
		return handler;
	}
}
