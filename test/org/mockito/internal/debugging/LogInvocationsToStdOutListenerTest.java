/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.PrintStream;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.exceptions.PrintableInvocation;
import org.mockitoutil.TestBase;

public class LogInvocationsToStdOutListenerTest extends TestBase {

	private static final String OTHER_LOCATION = "other location";
	private static final String LOCATION_OF_STUBBING = "location of stubbing";
	private static final String SOME_RETURN_VALUE = "some return value";
	private static final Exception SOME_EXCEPTION = new ThirdPartyException();

	@Test
	public void shouldPrintToStdOut() {
		assertSame(System.out, new LogInvocationsToStdOutListener().printStream);
	}

	@Test
	public void shouldPrintInvocationWithReturnValueToStream() {
		// given
		LogInvocationsToStdOutListener listener = createListenerWithMockedStream();
		PrintableInvocation invocation = createInvocationWithLocation();

		// when
		listener.invokingWithReturnValue(invocation, SOME_RETURN_VALUE,
				LOCATION_OF_STUBBING);

		// then
		assertIsPrinted(invocation.toString(), listener);
		assertIsPrinted(invocation.getLocation().toString(), listener);
		assertIsPrinted(SOME_RETURN_VALUE, listener);
		assertIsPrinted(LOCATION_OF_STUBBING, listener);
	}

	@Test
	public void shouldPrintInvocationWithExceptionToStream() {
		// given
		LogInvocationsToStdOutListener listener = createListenerWithMockedStream();
		PrintableInvocation invocation = createInvocationWithLocation();

		// when
		listener.invokingWithException(invocation, SOME_EXCEPTION,
				LOCATION_OF_STUBBING);

		// then
		assertIsPrinted(invocation.toString(), listener);
		assertIsPrinted(invocation.getLocation().toString(), listener);
		assertIsPrinted(SOME_EXCEPTION.getClass().getName(), listener);
		assertIsPrinted(LOCATION_OF_STUBBING, listener);
	}

	@Test
	public void shouldLogNumberOfInteractions() {
		// given
		LogInvocationsToStdOutListener listener = createListenerWithMockedStream();
		// when & then
		listener.invokingWithException(createInvocationWithLocation(), SOME_EXCEPTION, LOCATION_OF_STUBBING);
		assertIsPrinted("#1", listener);
		
		listener.invokingWithException(createInvocationWithLocation(), SOME_EXCEPTION, OTHER_LOCATION);
		assertIsPrinted("#2", listener);
		
		listener.invokingWithException(createInvocationWithLocation(), SOME_EXCEPTION, LOCATION_OF_STUBBING);
		assertIsPrinted("#3", listener);
	}
	
	private PrintableInvocation createInvocationWithLocation() {
		PrintableInvocation invocation = mock(PrintableInvocation.class);
		Location location = mock(Location.class);
		given(invocation.getLocation()).willReturn(location);
		return invocation;
	}

	private void assertIsPrinted(String printedString,
			LogInvocationsToStdOutListener listener) {
		assertContains(printedString, getEverythingThatWasPrinted(listener));
	}

	private String getEverythingThatWasPrinted(
			LogInvocationsToStdOutListener listener) {
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(listener.printStream, atLeastOnce()).println(captor.capture());
		StringBuffer concatenatedArgs = new StringBuffer();
		for (String arg : captor.getAllValues()) {
			concatenatedArgs.append(arg).append("\n");
		}
		return concatenatedArgs.toString();
	}

	private LogInvocationsToStdOutListener createListenerWithMockedStream() {
		LogInvocationsToStdOutListener listener = new LogInvocationsToStdOutListener();
		listener.printStream = mock(PrintStream.class);
		return listener;
	}

	private static class ThirdPartyException extends Exception {
		private static final long serialVersionUID = 3022739107688491354L;
	}
}