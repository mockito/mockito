/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.debugging;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.mockito.*;
import org.mockito.internal.invocation.*;
import org.mockito.invocation.*;
import org.mockitoutil.*;

/**
 * Ensures that custom listeners can be registered and will be called every
 * time a method on a mock is invoked.
 */
public class InvocationListenerCallbackTest extends TestBase {

    private static final String SOME_STRING_ARGUMENT = "some string argument";

	@Test
    public void shouldCallListenerWithCorrectCallback() throws Exception {
    	// given
    	InvocationListener listener = mock(InvocationListener.class);
		Foo foo = mock(Foo.class, withSettings().callback(listener));
    	
    	// when
		foo.doSomething(SOME_STRING_ARGUMENT);
    	
    	// then
		ArgumentCaptor<InvocationOnMock> captor = ArgumentCaptor.forClass(InvocationOnMock.class);
		verify(listener).invoking(captor.capture());
		assertSame(foo, captor.getValue().getMock());
		assertEquals(Foo.class.getMethod("doSomething", String.class), captor.getValue().getMethod());
    }
	
	@Test
	public void shouldCallMultipleListeners() throws Exception {
		// given
		InvocationListener listener1 = mock(InvocationListener.class);
		InvocationListener listener2 = mock(InvocationListener.class);
		Foo foo = mock(Foo.class, withSettings().callback(listener1).callback(listener2));
		
		// when
		foo.doSomething(SOME_STRING_ARGUMENT);
		
		// then
		verify(listener1).invoking(any(Invocation.class));
		verify(listener2).invoking(any(Invocation.class));
	}
}