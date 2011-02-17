/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.debugging;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.util.MockUtil;
import org.mockitoutil.TestBase;

/**
 * Tests the verbose logging of invocation on mock methods.
 * 
 * BEWARE: These tests rely on mocking the standard output.
 * While in a single-threaded environment the finally-blocks
 * should ensure, that the original stream is restored, there
 * is no guarantee for this in the parallel setting. Maybe, the
 * test class should be @Ignore'd by default ...
 */
public class VerboseLoggingOfInvocationsOnMockTest extends TestBase {

	private static final String ANOTHER_STRING_VALUE = "another string value";
	private static final String OTHER_STRING_VALUE = "other string value";
	private static final String SOME_STRING_VALUE = "some string value";
	
	@Mock
	UnrelatedClass unrelatedMock;
	
	@Test
	public void usage() {
		//given
		Foo foo = mock(Foo.class, withSettings().verboseLogging());
    	
        //when
    	foo.doSomething(SOME_STRING_VALUE);
    	foo.giveMeSomeString(OTHER_STRING_VALUE);
    	foo.giveMeSomeString(ANOTHER_STRING_VALUE);
	}
	
    @Test
    public void shouldPrintInvocationOnMockToStdOut() {
    	PrintStream original = System.out;
    	// FIXME cannot convince checkstyle to accept this code
//    	try {
    		ByteArrayOutputStream baos = setUpStreamAsStdOut();
    		shouldPrintInvocationOnMockToStdOut_withMockedStdout(baos);
//    	} finally {
//    		System.setOut(original);
//    	}
    }

    @Test
    public void shouldNotPrintInvocationOnMockWithoutSetting() {
    	PrintStream original = System.out;
    	// FIXME cannot convince checkstyle to accept this code
//    	try {
    		ByteArrayOutputStream baos = setUpStreamAsStdOut();
    		shouldNotPrintInvocationOnMockWithoutSetting_withMockedStdout(baos);
//    	} finally {
//    		System.setOut(original);
//    	}
    }

	private void shouldPrintInvocationOnMockToStdOut_withMockedStdout(ByteArrayOutputStream baos) {
		//given
		Foo foo = mock(Foo.class, withSettings().verboseLogging());
    	
        //when
    	foo.doSomething(SOME_STRING_VALUE);

        //then
    	assertStreamContainsMockName(foo, baos);
        assertContains("doSomething", baos.toString());
        assertContains(SOME_STRING_VALUE, baos.toString());
	}

    private void shouldNotPrintInvocationOnMockWithoutSetting_withMockedStdout(ByteArrayOutputStream baos) {
    	//given
    	Foo foo = mock(Foo.class, withSettings().verboseLogging());
    	
    	//when
    	foo.giveMeSomeString(SOME_STRING_VALUE);
    	unrelatedMock.unrelatedMethod(ANOTHER_STRING_VALUE);
    	
    	//then
    	assertStreamDoesNotContainMockName(unrelatedMock, baos);
    	assertNotContains("unrelatedMethod", baos.toString());
    	assertNotContains(ANOTHER_STRING_VALUE, baos.toString());
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
}