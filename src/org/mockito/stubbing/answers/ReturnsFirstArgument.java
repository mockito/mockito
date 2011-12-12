/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.stubbing.answers;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Returns first argument of a method (useful for mocking chains).
 * Throws exception if method has no arguments.
 * <p>
 * <pre class="code"><code class="java">
 *   //using constructor:
 *   when(mock.foo()).then(new ReturnsFirstArgument());
 *   //or static method:
 *   when(mock.foo()).then(returnFirstArgument());
 * <pre class="code"><code class="java">
 */
public class ReturnsFirstArgument implements Answer<Object> {

	@Override
	public Object answer(InvocationOnMock invocation) throws Throwable {
		Object[] arguments = invocation.getArguments();
		if (arguments.length == 0) {
			throw new MockitoException(
					"Wrong usage of ReturnsFirstArgument. No arguments were passed to the mocked method.");
		}
		return arguments[0];
	}

	public static ReturnsFirstArgument returnFirstArgument() {
		return new ReturnsFirstArgument();
	}
}
