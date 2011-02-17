/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.ArgumentMatcherStorage;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.invocation.InvocationListener;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;
import org.mockitoutil.TestBase;
@SuppressWarnings({"unchecked","serial"})
public class MockHandlerTest extends TestBase {
    
	@SuppressWarnings("rawtypes")
	private static final Answer SOME_ANSWER = mock(Answer.class);
	private static final StubbedInvocationMatcher SOME_RETURN_VALUE = mock(StubbedInvocationMatcher.class);
	private static final Invocation SOME_INVOCATION = mock(Invocation.class);
	@Mock private InvocationListener listener1;
	@Mock private InvocationListener listener2;

	@Test
    public void shouldRemoveVerificationModeEvenWhenInvalidMatchers() throws Throwable {
        //given
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
            //when
            handler.handle(invocation);
            
            //then
            fail();
        } catch (InvalidUseOfMatchersException e) {}
        
        assertNull(handler.mockingProgress.pullVerificationMode());
    }
    
    @Test
    public void shouldNotifyInvocationHandlerDuringStubVoid() throws Throwable {
    	// given
    	MockHandler<?> handler = createHandlerWithListeners(listener1, listener2);
    	stubWithInvocationDuringStubVoid(handler);
    	
    	
    	handler.handle(SOME_INVOCATION);
    	
    	// then
    	verify(listener1).invoking(SOME_INVOCATION);
    	verify(listener2).invoking(SOME_INVOCATION);
    }

	@Test
    public void shouldNotifyInvocationHandlerDuringVerification() throws Throwable {
    	// given
    	MockHandler<?> handler = createHandlerWithListeners(listener1, listener2);
    	stubProgressWithVerification(handler);
    	
    	// when
		handler.handle(SOME_INVOCATION);
    	
    	// then
    	verify(listener1).invoking(SOME_INVOCATION);
    	verify(listener2).invoking(SOME_INVOCATION);
    }
    
	@Test
    public void shouldNotifyInvocationHandlerDuringOrdinaryInvocationWithGivenReturnValue() throws Throwable {
    	// given
    	MockHandler<?> handler = createHandlerWithListeners(listener1, listener2);
    	stubOrdinaryInvocationWithGivenReturnValue(handler);
    	
    	// when
		handler.handle(SOME_INVOCATION);
    	
    	// then
    	verify(listener1).invoking(SOME_INVOCATION);
    	verify(listener2).invoking(SOME_INVOCATION);
    }

	@Test
	public void shouldNotifyInvocationHandlerDuringOrdinaryInvocationWithDefaultReturnValue() throws Throwable {
		// given
		MockHandler<?> handler = createHandlerWithListeners(listener1, listener2);
		stubOrdinaryInvocationWithDefaultReturnValue(handler);
		
		// when
		handler.handle(SOME_INVOCATION);
		
		// then
		verify(listener1).invoking(SOME_INVOCATION);
		verify(listener2).invoking(SOME_INVOCATION);
	}
	
	@Test(expected=MockitoException.class)
    public void shouldThrowMockitoExceptionWhenInvocationHandlerThrowsAnything() throws Throwable {
    	// given
		InvocationListener throwingListener = mock(InvocationListener.class);
		doThrow(new RuntimeException()).when(throwingListener).invoking(SOME_INVOCATION);
    	MockHandler<?> handler = createCorrectlyStubbedHandler(throwingListener);
    	
    	// when
		handler.handle(SOME_INVOCATION);
    }

	private MockHandler<?> createCorrectlyStubbedHandler(
			InvocationListener throwingListener) {
		MockHandler<?> handler = createHandlerWithListeners(throwingListener);
    	stubOrdinaryInvocationWithGivenReturnValue(handler);
		return handler;
	}

	private void stubOrdinaryInvocationWithGivenReturnValue(MockHandler<?> handler) {
		stubOrdinaryInvocationWithReturnValue(handler, SOME_RETURN_VALUE);
	}
	
	private void stubOrdinaryInvocationWithDefaultReturnValue(MockHandler<?> handler) {
		given(handler.getMockSettings().getDefaultAnswer()).willReturn(SOME_ANSWER);
		stubOrdinaryInvocationWithReturnValue(handler, null);
	}

	private void stubOrdinaryInvocationWithReturnValue(MockHandler<?> handler,
			StubbedInvocationMatcher value) {
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
		given(handler.matchersBinder.bindMatchers(any(ArgumentMatcherStorage.class), any(Invocation.class))).willReturn(invocationContainer);
		
		Invocation invocation = mock(Invocation.class);
		given(invocationContainer.getInvocation()).willReturn(invocation);
		
		given(invocation.isVoid()).willReturn(true);
	}

	@SuppressWarnings("rawtypes")
	private void stubWithPreviouslySetVoidAnswer(MockHandler handler) {
		List<Answer<?>> answers = new ArrayList<Answer<?>>();
    	answers.add(new DoesNothing());
    	handler.setAnswersForStubbing(answers);
	}
    
	private MockHandler<?> createHandlerWithListeners(InvocationListener... listener) {
		@SuppressWarnings("rawtypes")
		MockHandler<?> handler = new MockHandler(mock(MockSettingsImpl.class));
		handler.matchersBinder = mock(MatchersBinder.class);
		given(handler.getMockSettings().getInvocationListener()).willReturn(Arrays.asList(listener));
		return handler;
	}
}