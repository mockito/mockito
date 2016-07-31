/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.debugging;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.MockUtil;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.fail;
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
@RunWith(MockitoJUnitRunner.class)
public class VerboseLoggingOfInvocationsOnMockTest {

    private ByteArrayOutputStream output;
    private PrintStream original;

    @Mock UnrelatedClass unrelatedMock;

    @Before
    public void setUp() {
        original = System.out;
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    @After
    public void tearDown() {
        System.setOut(original);
    }

    @Test
    public void shouldNotPrintInvocationOnMockWithoutSetting() {
        // given
        Foo foo = mock(Foo.class, withSettings().verboseLogging());

        // when
        foo.giveMeSomeString("Klipsch");
        unrelatedMock.unrelatedMethod("Apple");

        // then
        Assertions.assertThat(printed())
                .doesNotContain(mockName(unrelatedMock))
                .doesNotContain("unrelatedMethod")
                .doesNotContain("Apple");
    }

    @Test
    public void shouldPrintUnstubbedInvocationOnMockToStdOut() {
        // given
        Foo foo = mock(Foo.class, withSettings().verboseLogging());

        // when
        foo.doSomething("Klipsch");

        // then
        Assertions.assertThat(printed())
                .contains(getClass().getName())
                .contains(mockName(foo))
                .contains("doSomething")
                .contains("Klipsch");
    }

    @Test
    public void shouldPrintStubbedInvocationOnMockToStdOut() {
        // given
        Foo foo = mock(Foo.class, withSettings().verboseLogging());
        given(foo.giveMeSomeString("Klipsch")).willReturn("earbuds");

        // when
        foo.giveMeSomeString("Klipsch");

        // then
        Assertions.assertThat(printed())
                .contains(getClass().getName())
                .contains(mockName(foo))
                .contains("giveMeSomeString")
                .contains("Klipsch")
                .contains("earbuds");
    }

    @Test
    public void shouldPrintThrowingInvocationOnMockToStdOut() {
        // given
        Foo foo = mock(Foo.class, withSettings().verboseLogging());
        doThrow(new ThirdPartyException()).when(foo).doSomething("Klipsch");

        try {
            // when
            foo.doSomething("Klipsch");
            fail("Exception excepted.");
        } catch (ThirdPartyException e) {
            // then
            Assertions.assertThat(printed())
                    .contains(getClass().getName())
                    .contains(mockName(foo))
                    .contains("doSomething")
                    .contains("Klipsch")
                    .contains(ThirdPartyException.class.getName());
        }
    }

    @Test
    public void shouldPrintRealInvocationOnSpyToStdOut() {
        // given
        FooImpl fooSpy = mock(FooImpl.class,
                withSettings().spiedInstance(new FooImpl()).verboseLogging());
        doCallRealMethod().when(fooSpy).doSomething("Klipsch");
        
        // when
        fooSpy.doSomething("Klipsch");
        
        // then
        Assertions.assertThat(printed())
                .contains(getClass().getName())
                .contains(mockName(fooSpy))
                .contains("doSomething")
                .contains("Klipsch");
    }

    @Test
    public void usage() {
        // given
        Foo foo = mock(Foo.class, withSettings().verboseLogging());
        given(foo.giveMeSomeString("Apple")).willReturn(
                "earbuds");

        // when
        foo.giveMeSomeString("Shure");
        foo.giveMeSomeString("Apple");
        foo.doSomething("Klipsch");
    }

    private String printed() {
        return output.toString();
    }

    private String mockName(Object mock) {
        return MockUtil.getMockName(mock).toString();
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
            return null;
        }

        public void doSomething(String param) {
        }
    }
}
