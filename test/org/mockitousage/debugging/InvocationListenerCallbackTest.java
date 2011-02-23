/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.debugging;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;

import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.matchers.Contains;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.InstanceOf;
import org.mockito.invocation.InvocationListener;
import org.mockitousage.debugging.VerboseLoggingOfInvocationsOnMockTest.ThirdPartyException;
import org.mockitoutil.TestBase;

/**
 * Ensures that custom listeners can be registered and will be called every time
 * a method on a mock is invoked.
 */
public class InvocationListenerCallbackTest extends TestBase {

	private static final String SOME_RETURN_VALUE = "some return value";
	private static final String SOME_STRING_ARGUMENT = "some string argument";

	@Test
	public void givenInvocationReturningValue_shouldCallSingleListenerWithCorrectCallback() throws Exception {
		// given
		// Cannot use a mockito-mock here: during stubbing, the listener will be called
		// and mockito will confuse the mocks.
		RememberingListener listener = new RememberingListener();
		Foo foo = mock(Foo.class, withSettings().callback(listener));
		given(foo.giveMeSomeString(SOME_STRING_ARGUMENT)).willReturn(SOME_RETURN_VALUE);

		// when
		foo.giveMeSomeString(SOME_STRING_ARGUMENT);

		assertHasBeenNotified(listener, new InstanceOf(PrintableInvocation.class),
				new Equals(SOME_RETURN_VALUE), new Contains(getClass().getSimpleName().toString()));
	}

	@Test
	public void givenInvocationReturningValue_shouldCallMultipleListeners() throws Exception {
		// given
		// Cannot use a mockito-mock here: during stubbing, the listener will be called
		// and mockito will confuse the mocks.
		RememberingListener listener1 = new RememberingListener();
		RememberingListener listener2 = new RememberingListener();
		Foo foo = mock(Foo.class, withSettings().callback(listener1).callback(listener2));
		given(foo.giveMeSomeString(SOME_STRING_ARGUMENT)).willReturn(SOME_RETURN_VALUE);

		// when
		foo.giveMeSomeString(SOME_STRING_ARGUMENT);

		// then
		assertHasBeenNotified(listener1, new InstanceOf(PrintableInvocation.class),
				new Equals(SOME_RETURN_VALUE), new Contains(getClass().getSimpleName().toString()));
		assertHasBeenNotified(listener2, new InstanceOf(PrintableInvocation.class),
				new Equals(SOME_RETURN_VALUE), new Contains(getClass().getSimpleName().toString()));
	}

	@Test
	public void givenInvocationThrowingException_shouldCallSingleListenerWithCorrectCallback() throws Exception {
		// given
		InvocationListener listener = mock(InvocationListener.class);
		RuntimeException expectedException = new ThirdPartyException();
		Foo foo = mock(Foo.class, withSettings().callback(listener));
		doThrow(expectedException).when(foo).doSomething(SOME_STRING_ARGUMENT);

		// when
		try {
			foo.doSomething(SOME_STRING_ARGUMENT);
			fail("Exception expected.");
		} catch (ThirdPartyException actualException) {
			// then
			assertSame(expectedException, actualException);
			verify(listener).invokingWithException(isA(PrintableInvocation.class), eq(actualException),
					isA(String.class));
		}
	}

	@Test
	public void givenInvocationThrowingException_shouldCallMultipleListeners() throws Exception {
		// given
		InvocationListener listener1 = mock(InvocationListener.class);
		InvocationListener listener2 = mock(InvocationListener.class);
		Foo foo = mock(Foo.class, withSettings().callback(listener1).callback(listener2));
		doThrow(new ThirdPartyException()).when(foo).doSomething(SOME_STRING_ARGUMENT);

		// when
		try {
			foo.doSomething(SOME_STRING_ARGUMENT);
			fail("Exception expected.");
		} catch (ThirdPartyException actualException) {
			// then
			verify(listener1).invokingWithException(isA(PrintableInvocation.class), isA(RuntimeException.class),
					isA(String.class));
			verify(listener2).invokingWithException(isA(PrintableInvocation.class), isA(RuntimeException.class),
					isA(String.class));
		}
	}
	
	private void assertHasBeenNotified(RememberingListener listener, InstanceOf m, Equals m2, Contains m3) {
		assertThat(listener.invocation, m);
		assertThat(listener.returnValue, m2);
		assertThat(listener.locationOfStubbing, m3);
	}

	static class RememberingListener implements InvocationListener {
		
		PrintableInvocation invocation;
		Object returnValue;
		String locationOfStubbing;

		public void invokingWithReturnValue(PrintableInvocation invocation, Object returnValue,
				String locationOfStubbing) {
			this.invocation = invocation;
			this.returnValue = returnValue;
			this.locationOfStubbing = locationOfStubbing;
		}

		public void invokingWithException(PrintableInvocation invocation, Exception exception, String locationOfStubbing) {
			throw new UnsupportedOperationException();
		}
		
	}
}