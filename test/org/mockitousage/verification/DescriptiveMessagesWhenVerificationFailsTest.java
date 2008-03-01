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
import org.mockito.exceptions.verification.NeverWantedButInvoked;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.ArgumentsAreDifferentException;
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
        } catch (ArgumentsAreDifferentException e) {
            String expected =
                    "\n" +
                    "Argument(s) are different!" +
                    "\n" +
                    "Method: IMethods.twoArgumentMethod(...)" +
                    "\n" +
                    "All wanted arguments:" +
                    "\n" +
                    "    1: 1" +
                    "\n" +
                    "    2: 1000";

            assertEquals(expected, e.getMessage());

            assertEquals(e.getCause().getClass(), WantedDiffersFromActual.class);

            String expectedCause =
                    "\n" +
                    "All actual arguments:" +
                    "\n" +
                    "    1: 1" +
                    "\n" +
                    "    2: 2";

            assertEquals(expectedCause, e.getCause().getMessage());
        }
    }

    @Test
    public void shouldPrintActualAndWantedWhenActualMethodNameAndWantedMethodNameAreTheSame() {
        mock.simpleMethod();

        try {
            verify(mock).simpleMethod("test");
            fail();
        } catch (ArgumentsAreDifferentException e) {
            assertThat(e, messageContains("    1: \"test\""));
            assertThat(e, causeMessageContains("    <no arguments>"));
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
        } catch (ArgumentsAreDifferentException e) {
            assertThat(e, messageContains("1: 3"));
            assertThat(e, messageContains("2: 1000"));
            assertThat(e, causeMessageContains("1: 3"));
            assertThat(e, causeMessageContains("2: 3"));
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
        } catch (ArgumentsAreDifferentException e) {
            assertThat(e, messageContains("1: [false, false, false]"));
            assertThat(e, causeMessageContains("1: [true, false, false]"));
        }
    }

    @Test
    public void shouldPrintMethodWhenMissingInvocationWithVarargMatcher() {
        mock.varargsString(10, "one", "two");

        try {
            verify(mock).varargsString(10, "two", "one");
            fail();
        } catch (ArgumentsAreDifferentException e) {
            assertThat(e, messageContains("1: 10"));
            assertThat(e, messageContains("2: \"two\""));
            assertThat(e, messageContains("3: \"one\""));
            
            assertThat(e, causeMessageContains("1: 10"));
            assertThat(e, causeMessageContains("2: \"one\""));
            assertThat(e, causeMessageContains("3: \"two\""));
        }
    }

    @Test
    public void shouldPrintMethodWhenMissingInvocationWithMatcher() {
        mock.simpleMethod("foo");

        try {
            verify(mock).simpleMethod(matches("burrito from Exmouth"));
            fail();
        } catch (ArgumentsAreDifferentException e) {
            assertThat(e, messageContains("1: matches(\"burrito from Exmouth\")"));
            assertThat(e, causeMessageContains("1: \"foo\""));
        }
    }

    @Test
    public void shouldPrintNullArguments() throws Exception {
        mock.simpleMethod(null, null);
        try {
            verify(mock).simpleMethod("test");
            fail();
        } catch (ArgumentsAreDifferentException e) {
            assertThat(e, causeMessageContains("1: null"));
            assertThat(e, causeMessageContains("2: null"));
        }
    }

    @Test
    public void shouldPrintTypesWhenMethodSupposablyTheSame() throws Exception {
        mock.varargs((Object[]) new Object[] {});
        try {
            verify(mock).varargs((String[]) new String[] {});
            fail();
        } catch(ArgumentsAreDifferentException e) {
            assertThat(e, messageContains("1: class [Ljava.lang.String;"));
            assertThat(e, causeMessageContains("1: class [Ljava.lang.Object;"));
        }
    }
    
    @Test
    public void shouldSayNeverWantedButInvoked() throws Exception {
        mock.simpleMethod(1);
    
        verify(mock, never()).simpleMethod(2);
        try {
            verify(mock, never()).simpleMethod(1);
            fail();
        } catch (NeverWantedButInvoked e) {
            assertThat(e, messageContains("Never wanted but invoked!"));
            assertThat(e, causeMessageContains("Undesired invocation"));
        }
    }
}