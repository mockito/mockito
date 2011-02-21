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
	 * Called when a method on the listener's mock is invoked.
	 * 
	 * Exceptions during this callback are treated as fatal errors. 
	 * 
	 * TODO update documentation to parameters
	 * 
	 * @param invocation information on the happening method call, never {@code null}.
	 * @throws RuntimeException on fatal errors
	 */
	void invokingWithReturnValue(PrintableInvocation invocation, Object returnValue, String locationOfStubbing);
	
	void invokingWithException(PrintableInvocation invocation, Exception exception, String locationOfStubbing);

}
