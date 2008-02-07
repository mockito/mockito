/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import static org.junit.Assert.*;
import static org.mockito.AdditionalMatchers.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.util.ExtraMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.TestBase;
import org.mockito.exceptions.cause.UndesiredInvocation;
import org.mockito.exceptions.cause.WantedDiffersFromActual;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.InvocationDiffersFromActual;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockitousage.IMethods;

public class DescriptiveMessagesWhenVerificationFailsTest extends TestBase {

    private IMethods mock;

    @Before
    public void setup() {
        mock = Mockito.mock(IMethods.class);
    }

    @Test
    public void shouldPrintMethodName() {
        try {
            verify(mock).simpleMethod();
            fail();
        } catch (WantedButNotInvoked e) {
            String actualMessage = e.getMessage();
            String expectedMessage =
                    "\n" +
                    "Wanted but not invoked:" +
                    "\n" +
                    "IMethods.simpleMethod()";
            assertEquals(expectedMessage, actualMessage);
        }
    }

    private class SomeClass {
        public String toString() {
            return "SomeClass instance";
        }
    }

    @Test
    public void shouldPrintMethodNameAndArguments() {
        try {
            verify(mock).threeArgumentMethod(12, new SomeClass(), "xx");
            fail();
        } catch (WantedButNotInvoked e) {
            assertThat(e, messageContains("IMethods.threeArgumentMethod(12, SomeClass instance, \"xx\")"));
        }
    }

    @Test
    public void shouldPrintActualAndWantedWhenTheDifferenceIsAboutArguments() {
        mock.oneArg(true);
        mock.twoArgumentMethod(1, 2);

        verify(mock).oneArg(true);
        try {
            verify(mock).twoArgumentMethod(1, 1000);
            fail();
        } catch (InvocationDiffersFromActual e) {
            String expected =
                    "\n" +
                    "Invocation differs from actual" +
                    "\n" +
                    "Wanted invocation:" +
                    "\n" +
                    "IMethods.twoArgumentMethod(1, 1000)";

            assertEquals(expected, e.getMessage());

            assertEquals(e.getCause().getClass(), WantedDiffersFromActual.class);

            String expectedCause =
                    "\n" +
                    "Actual invocation:" +
                    "\n" +
                    "IMethods.twoArgumentMethod(1, 2)";

            assertEquals(expectedCause, e.getCause().getMessage());
        }
    }

    @Test
    public void shouldPrintActualAndWantedWhenActualMethodNameAndWantedMethodNameAreTheSame() {
        mock.simpleMethod();

        try {
            verify(mock).simpleMethod("test");
            fail();
        } catch (InvocationDiffersFromActual e) {
            assertThat(e, messageContains("IMethods.simpleMethod(\"test\")"));
            assertThat(e, causeMessageContains("IMethods.simpleMethod()"));
        }
    }

    @Test
    public void shouldPrintActualAndUnverifiedWantedWhenTheDifferenceIsAboutArguments() {
        mock.twoArgumentMethod(1, 1);
        mock.twoArgumentMethod(2, 2);
        mock.twoArgumentMethod(3, 3);

        verify(mock).twoArgumentMethod(1, 1);
        verify(mock).twoArgumentMethod(2, 2);
        try {
            verify(mock).twoArgumentMethod(3, 1000);
            fail();
        } catch (InvocationDiffersFromActual e) {
            assertThat(e, messageContains("IMethods.twoArgumentMethod(3, 1000)"));
            assertThat(e, causeMessageContains("IMethods.twoArgumentMethod(3, 3)"));
        }
    }

    @Test
    public void shouldPrintFirstUnexpectedInvocation() {
        mock.oneArg(true);
        mock.oneArg(false);
        mock.threeArgumentMethod(1, "2", "3");

        verify(mock).oneArg(true);
        try {
            verifyNoMoreInteractions(mock);
            fail();
        } catch (NoInteractionsWanted e) {
            String expectedMessage =
                    "\n" +
                    "No interactions wanted";
            assertEquals(expectedMessage, e.getMessage());

            assertEquals(e.getCause().getClass(), UndesiredInvocation.class);

            String expectedCause =
                    "\n" +
                    "Undesired invocation:" +
                    "\n" +
                    "IMethods.oneArg(false)";
            assertEquals(expectedCause, e.getCause().getMessage());
        }
    }

    @Test
    public void shouldPrintFirstUnexpectedInvocationWhenVerifyingZeroInteractions() {
        mock.twoArgumentMethod(1, 2);
        mock.threeArgumentMethod(1, "2", "3");

        try {
            verifyZeroInteractions(mock);
            fail();
        } catch (NoInteractionsWanted e) {
            String expected =
                    "\n" +
                    "No interactions wanted";

            assertEquals(e.getMessage(), expected);

            String expectedCause =
                "\n" +
                "Undesired invocation:" +
                "\n" +
                "IMethods.twoArgumentMethod(1, 2)";

            assertEquals(e.getCause().getMessage(), expectedCause);
        }
    }

    @Test
    public void shouldPrintMethodNameWhenVerifyingAtLeastOnce() throws Exception {
        try {
            verify(mock, atLeastOnce()).twoArgumentMethod(1, 2);
            fail();
        } catch (WantedButNotInvoked e) {
            assertThat(e, messageContains("IMethods.twoArgumentMethod(1, 2)"));
        }
    }

    @Test
    public void shouldPrintMethodWhenMatcherUsed() throws Exception {
        try {
            verify(mock, atLeastOnce()).twoArgumentMethod(anyInt(), eq(100));
            fail();
        } catch (WantedButNotInvoked e) {
            String actualMessage = e.getMessage();
            String expectedMessage =
                "\n" +
                "Wanted but not invoked:" +
                "\n" +
                "IMethods.twoArgumentMethod(<any>, 100)";
            assertEquals(expectedMessage, actualMessage);
        }
    }

    @Test
    public void shouldPrintMethodWhenMissingInvocationWithArrayMatcher() {
        mock.oneArray(new boolean[] { true, false, false });

        try {
            verify(mock).oneArray(aryEq(new boolean[] { false, false, false }));
            fail();
        } catch (InvocationDiffersFromActual e) {
            assertThat(e, messageContains("IMethods.oneArray([false, false, false])"));
            assertThat(e, causeMessageContains("IMethods.oneArray([true, false, false])"));
        }
    }

    @Test
    public void shouldPrintMethodWhenMissingInvocationWithVarargMatcher() {
        mock.varargsString(10, "one", "two");

        try {
            verify(mock).varargsString(10, "two", "one");
            fail();
        } catch (InvocationDiffersFromActual e) {
            assertThat(e, messageContains("IMethods.varargsString(10, \"two\", \"one\")"));
            assertThat(e, causeMessageContains("IMethods.varargsString(10, \"one\", \"two\")"));
        }
    }

    @Test
    public void shouldPrintMethodWhenMissingInvocationWithMatcher() {
        mock.simpleMethod("foo");

        try {
            verify(mock).simpleMethod(matches("burrito"));
            fail();
        } catch (InvocationDiffersFromActual e) {
            assertThat(e, messageContains("IMethods.simpleMethod(matches(\"burrito\"))"));
            assertThat(e, causeMessageContains("IMethods.simpleMethod(\"foo\")"));
        }
    }

    @Test
    public void shouldPrintNullArguments() throws Exception {
        mock.simpleMethod(null, null);
        try {
            verify(mock).simpleMethod("test");
            fail();
        } catch (InvocationDiffersFromActual e) {
            assertThat(e, causeMessageContains("simpleMethod(null, null)"));
        }
    }

    @Test
    public void shouldPrintTypesWhenMethodSupposablyTheSame() throws Exception {
        mock.varargs((Object[]) new Object[] {});
        try {
            verify(mock).varargs((String[]) new String[] {});
            fail();
        } catch(InvocationDiffersFromActual e) {
            assertThat(e, messageContains("IMethods.varargs(class [Ljava.lang.String;)"));
            assertThat(e, causeMessageContains("IMethods.varargs(class [Ljava.lang.Object;)"));
        }
    }
}