/*
 * Copyright (c) 2011 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.invocation;

import org.mockito.MockSettings;
import org.mockito.exceptions.PrintableInvocation;

/**
 * This listener can be notified of method invocations on a mock.
 * 
 * For this to happen, it must be registered using {@link MockSettings#callback(InvocationListener)}.
 */
public interface InvocationListener {
	
	/**
	 * Called when a method on the listener's mock was invoked and returned normally.
	 * 
	 * Exceptions during this invocationListeners are treated as fatal errors.
	 * 
	 * @param invocation information on the happening method call, never {@code null}
	 * @param returnValue whatever it was that the method returned, may be {@code null}
	 * @param locationOfStubbing Indicates where the method was stubbed, and {@code null} if it was not
	 * @throws RuntimeException on fatal errors
	 */
	void invokingWithReturnValue(PrintableInvocation invocation, Object returnValue, String locationOfStubbing);
	
	/**
	 * Called when a method on the listener's mock was invoked and threw an exception.
	 * 
	 * Errors are not reported, as they usually indicate some severe VM error, that ought
	 * to be propagated and is typically not thrown by your code to begin with.
	 * 
	 * Note that the exception is not necessarily caused by stubbing the method with it, but may also
	 * be the result of incorrect usage of the mockito API or even a bug inside mockito.
	 * 
	 * Exceptions during this invocationListeners are treated as fatal errors.
	 * 
	 * @param invocation information on the happening method call, never {@code null}
	 * @param exception the exception that was thrown
	 * @param locationOfStubbing Indicates where the method was stubbed, and {@code null} if it was not
	 * @throws RuntimeException on fatal errors
	 */
	void invokingWithException(PrintableInvocation invocation, Exception exception, String locationOfStubbing);
}
