/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.stubbing.answers;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.stubbing.answers.ReturnsFirstArgument.returnFirstArgument;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockitoutil.TestBase;

public class ReturnsFirstArgumentTest extends TestBase {

	interface Chain {
		String process();
		
		String process(String arg);
		
		String process(String arg1, String arg2, String arg3);
	}

	@Mock
	private Chain chain;

	@Test
	public void shouldReturnFirstArgumentForOneArgumentMethod() throws Exception {
		// given
		when(chain.process(anyString())).then(returnFirstArgument());

		// when
		String result = chain.process("test");

		// then
		assertEquals("test", result);
	}

	@Test
	public void shouldReturnFirstArgumentForMultipleArgumentsMethod() throws Exception {
		// given
		when(chain.process(anyString(), anyString(), anyString())).then(returnFirstArgument());

		// when
		String result = chain.process("test", "other", "other");

		// then
		assertEquals("test", result);
	}
	
	@Test(expected=MockitoException.class)
	public void shouldScreamForMethodWithoutArguments() throws Exception {
		// given
		when(chain.process()).then(returnFirstArgument());
		
		// when
		chain.process();
	}

}
