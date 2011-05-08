/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.debugging;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.matchers.Contains;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.InstanceOf;
import org.mockito.invocation.InvocationListener;
import org.mockito.invocation.MethodCallReport;
import org.mockitousage.debugging.VerboseLoggingOfInvocationsOnMockTest.ThirdPartyException;
import org.mockitoutil.TestBase;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

/**
 * Ensures that custom listeners can be registered and will be called every time
 * a method on a mock is invoked.
 */
public class InvocationListenerCallbackTest extends TestBase {

    @Test
	public void givenInvocationReturningValue_shouldCallSingleListenerWithCorrectCallback() throws Exception {
		// given
		// Cannot use a mockito-mock here: during stubbing, the listener will be called
		// and mockito will confuse the mocks.
		RememberingListener listener = new RememberingListener();
		Foo foo = mock(Foo.class, withSettings().invocationListeners(listener));
		given(foo.giveMeSomeString("argument")).willReturn("returned");

		// when
		foo.giveMeSomeString("argument");

		assertHasBeenNotified(listener, new InstanceOf(PrintableInvocation.class),
				new Equals("returned"), new Contains(getClass().getSimpleName()));
	}

	@Test
	public void givenInvocationReturningValue_shouldCallMultipleListeners() throws Exception {
		// given
		// Cannot use a mockito-mock here: during stubbing, the listener will be called
		// and mockito will confuse the mocks.
		RememberingListener listener1 = new RememberingListener();
		RememberingListener listener2 = new RememberingListener();
		Foo foo = mock(Foo.class, withSettings().invocationListeners(listener1, listener2));
		given(foo.giveMeSomeString("argument")).willReturn("returned");

		// when
		foo.giveMeSomeString("argument");

		// then
		assertHasBeenNotified(listener1, new InstanceOf(PrintableInvocation.class),
				new Equals("returned"), new Contains(getClass().getSimpleName()));
		assertHasBeenNotified(listener2, new InstanceOf(PrintableInvocation.class),
				new Equals("returned"), new Contains(getClass().getSimpleName()));
	}

	@Test
    @Ignore("not anymore, waiting complete refactoring to be removed")
	public void givenInvocationThrowingException_shouldCallSingleListenerWithCorrectCallback() throws Exception {
		// given
		InvocationListener listener = mock(InvocationListener.class);
		RuntimeException expectedException = new ThirdPartyException();
		Foo foo = mock(Foo.class, withSettings().invocationListeners(listener));
		doThrow(expectedException).when(foo).doSomething("argument");

		// when
		try {
			foo.doSomething("argument");
			fail("Exception expected.");
		} catch (ThirdPartyException actualException) {
			// then
			assertSame(expectedException, actualException);
			verify(listener).reportInvocation(isA(MethodCallReport.class));
		}
	}

	@Test
	public void givenInvocationThrowingException_shouldCallMultipleListeners() throws Exception {
		// given
		InvocationListener listener1 = mock(InvocationListener.class, "listener1");
		InvocationListener listener2 = mock(InvocationListener.class, "listener2");
		Foo foo = mock(Foo.class, withSettings().invocationListeners(listener1, listener2));
		doThrow(new ThirdPartyException()).when(foo).doSomething("argument");

		// when
		try {
			foo.doSomething("argument");
			fail("Exception expected.");
		} catch (ThirdPartyException actualException) {
			// then
            InOrder orderedVerify = inOrder(listener1, listener2);
            orderedVerify.verify(listener1).reportInvocation(any(MethodCallReport.class));
			orderedVerify.verify(listener2).reportInvocation(any(MethodCallReport.class));
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

		public void reportInvocation(MethodCallReport mcr) {
			this.invocation = mcr.getInvocation();
			this.returnValue = mcr.getReturnedValue();
			this.locationOfStubbing = mcr.getLocationOfStubbing();
		}

	}
}
