/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.debugging;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.util.MockUtil;
import org.mockitoutil.TestBase;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * Tests the verbose logging of invocation on mock methods.
 * 
 * BEWARE: These tests rely on mocking the standard output. While in a
 * single-threaded environment the Before/After-contract ensures, that the
 * original output stream is restored, there is no guarantee for this
 * in the parallel setting.
 * Maybe, the test class should be @Ignore'd by default ...
 */
public class VerboseLoggingOfInvocationsOnMockTest extends TestBase {

	private static final String SOME_RETURN_VALUE = "some return value";
	private static final String ANOTHER_STRING_VALUE = "another string value";
	private static final String OTHER_STRING_VALUE = "other string value";
	private static final String SOME_STRING_VALUE = "some string value";

	private PrintStream original;

	@Mock UnrelatedClass unrelatedMock;
	
	@Before
	public void setUp() {
		original = System.out;
	}

	@After
	public void tearDown() {
		System.setOut(original);
	}

	@Test
	public void shouldNotPrintInvocationOnMockWithoutSetting() {
		ByteArrayOutputStream baos = setUpStreamAsStdOut();
		// given
		Foo foo = mock(Foo.class, withSettings().verboseLogging());

		// when
		foo.giveMeSomeString(SOME_STRING_VALUE);
		unrelatedMock.unrelatedMethod(ANOTHER_STRING_VALUE);

		// then
		assertStreamDoesNotContainMockName(unrelatedMock, baos);
		assertNotContains("unrelatedMethod", baos.toString());
		assertNotContains(ANOTHER_STRING_VALUE, baos.toString());
	}

	@Test
	public void shouldPrintUnstubbedInvocationOnMockToStdOut() {
		ByteArrayOutputStream baos = setUpStreamAsStdOut();
		// given
		Foo foo = mock(Foo.class, withSettings().verboseLogging());

		// when
		foo.doSomething(SOME_STRING_VALUE);

		// then
		assertStreamContainsClassName(getClass(), baos);
		assertStreamContainsMockName(foo, baos);
		assertContains("doSomething", baos.toString());
		assertContains(SOME_STRING_VALUE, baos.toString());
	}

	@Test
	public void shouldPrintStubbedInvocationOnMockToStdOut() {
		ByteArrayOutputStream baos = setUpStreamAsStdOut();
		// given
		Foo foo = mock(Foo.class, withSettings().verboseLogging());
		given(foo.giveMeSomeString(SOME_STRING_VALUE)).willReturn(SOME_RETURN_VALUE);

		// when
		foo.giveMeSomeString(SOME_STRING_VALUE);

		// then
		assertStreamContainsClassName(getClass(), baos);
		assertStreamContainsMockName(foo, baos);
		assertContains("giveMeSomeString", baos.toString());
		assertContains(SOME_STRING_VALUE, baos.toString());
		assertContains(SOME_RETURN_VALUE, baos.toString());
	}

	@Test
	public void shouldPrintThrowingInvocationOnMockToStdOut() {
		ByteArrayOutputStream baos = setUpStreamAsStdOut();
		// given
		Foo foo = mock(Foo.class, withSettings().verboseLogging());
		doThrow(new ThirdPartyException()).when(foo).doSomething(SOME_STRING_VALUE);

		try {
			// when
			foo.doSomething(SOME_STRING_VALUE);
			fail("Exception erwartet.");
		} catch (ThirdPartyException e) {
			// then
			assertStreamContainsClassName(getClass(), baos);
			assertStreamContainsMockName(foo, baos);
			assertContains("doSomething", baos.toString());
			assertContains(SOME_STRING_VALUE, baos.toString());
			assertStreamContainsClassName(ThirdPartyException.class, baos);
		}
	}

	@Test
	public void shouldPrintRealInvocationOnSpyToStdOut() {
		ByteArrayOutputStream baos = setUpStreamAsStdOut();
		// given
		FooImpl fooSpy = mock(FooImpl.class,
				withSettings().spiedInstance(new FooImpl()).verboseLogging());
		doCallRealMethod().when(fooSpy).doSomething(SOME_STRING_VALUE);
		
		// when
		fooSpy.doSomething(SOME_STRING_VALUE);
		
		// then
		assertStreamContainsClassName(getClass(), baos);
		assertStreamContainsMockName(fooSpy, baos);
		assertContains("doSomething", baos.toString());
		assertContains(SOME_STRING_VALUE, baos.toString());
	}

	@Test
	public void usage() {
		// given
		Foo foo = mock(Foo.class, withSettings().verboseLogging());
		given(foo.giveMeSomeString(ANOTHER_STRING_VALUE)).willReturn(
				SOME_RETURN_VALUE);

		// when
		foo.giveMeSomeString(OTHER_STRING_VALUE);
		foo.giveMeSomeString(ANOTHER_STRING_VALUE);
		foo.doSomething(SOME_STRING_VALUE);
	}


    private void assertStreamContainsClassName(Class<?> clazz,ByteArrayOutputStream baos) {
		assertContains(clazz.getName().toString(), baos.toString());
	}

	private ByteArrayOutputStream setUpStreamAsStdOut() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(baos));
		return baos;
	}

	private void assertStreamContainsMockName(Object mock, ByteArrayOutputStream baos) {
		assertContains(new MockUtil().getMockName(mock).toString(), baos.toString());
	}

	private void assertStreamDoesNotContainMockName(Object mock, ByteArrayOutputStream baos) {
		assertNotContains(new MockUtil().getMockName(mock).toString(), baos.toString());
	}

	private static class UnrelatedClass {
		void unrelatedMethod(String anotherStringValue) {

		}
	}

	/**
	 * An exception that isn't defined by Mockito or the JDK and therefore does
	 * not appear in the logging result by chance alone.
	 */
	static class ThirdPartyException extends RuntimeException {
		private static final long serialVersionUID = 2160445705646210847L;
	}

	static class FooImpl implements Foo {

		public String giveMeSomeString(String param) {
			// nothing to do
			return null;
		}

		public void doSomething(String param) {
			// nothing to do
		}

	}
}
