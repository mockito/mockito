/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import static org.mockito.AdditionalMatchers.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.cause.ActualArgumentsAreDifferent;
import org.mockito.exceptions.cause.UndesiredInvocation;
import org.mockito.exceptions.verification.NeverWantedButInvoked;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

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
                    "iMethods.simpleMethod();";
            assertEquals(expectedMessage, actualMessage);
        }
    }

    private class Foo {
        public String toString() {
            return "foo";
        }
    }

    @Test
    public void shouldPrintMethodNameAndArguments() {
        try {
            verify(mock).threeArgumentMethod(12, new Foo(), "xx");
            fail();
        } catch (WantedButNotInvoked e) {
            assertThat(e, messageContains("iMethods.threeArgumentMethod(12, foo, \"xx\")"));
        }
    }

    @Test
    public void shouldPrintActualAndWantedInLine() {
        mock.varargs(1, 2);

        try {
            verify(mock).varargs(1, 1000);
            fail();
        } catch (ArgumentsAreDifferent e) {
            String expected =
                    "\n" +
                    "Argument(s) are different! Wanted:" +
                    "\n" +
                    "iMethods.varargs(1, 1000);";

            assertEquals(expected, e.getMessage());

            assertEquals(e.getCause().getClass(), ActualArgumentsAreDifferent.class);

            String expectedCause =
                    "\n" +
                    "Actual invocation has different arguments:" +
                    "\n" +
                    "iMethods.varargs(1, 2);";

            assertEquals(expectedCause, e.getCause().getMessage());
        }
    }
    
    @Test
    public void shouldPrintActualAndWantedInMultipleLines() {
        mock.varargs("this is very long string", "this is another very long string");

        try {
            verify(mock).varargs("x", "y", "z");
            fail();
        } catch (ArgumentsAreDifferent e) {
            String expected =
                    "\n" +
                    "Argument(s) are different! Wanted:" +
                    "\n" +
                    "iMethods.varargs(" +
                    "\n" +
                    "    \"x\"," +
                    "\n" +
                    "    \"y\"," +
                    "\n" +
                    "    \"z\"" +
                    "\n" +
                    ");";

            assertEquals(expected, e.getMessage());

            assertEquals(e.getCause().getClass(), ActualArgumentsAreDifferent.class);

            String expectedCause =
                    "\n" +
                    "Actual invocation has different arguments:" +
                    "\n" +
                    "iMethods.varargs(" +
                    "\n" +
                    "    \"this is very long string\"," +
                    "\n" +
                    "    \"this is another very long string\"" +
                    "\n" +
                    ");";

            assertEquals(expectedCause, e.getCause().getMessage());
        }
    }

    @Test
    public void shouldPrintActualAndWantedWhenActualMethodNameAndWantedMethodNameAreTheSame() {
        mock.simpleMethod();

        try {
            verify(mock).simpleMethod(10);
            fail();
        } catch (ArgumentsAreDifferent e) {
            assertThat(e, messageContains("simpleMethod(10)"));
            assertThat(e, causeMessageContains("simpleMethod()"));
        }
    }

    @Test
    public void shouldPrintActualAndUnverifiedWantedWhenTheDifferenceIsAboutArguments() {
        mock.twoArgumentMethod(1, 1);
        mock.twoArgumentMethod(2, 2);

        verify(mock).twoArgumentMethod(1, 1);
        try {
            verify(mock).twoArgumentMethod(2, 1000);
            fail();
        } catch (ArgumentsAreDifferent e) {
            assertThat(e, messageContains("(2, 1000)"));
            assertThat(e, causeMessageContains("(2, 2)"));
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
                    "iMethods.oneArg(false);";
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
                "iMethods.twoArgumentMethod(1, 2);";

            assertEquals(e.getCause().getMessage(), expectedCause);
        }
    }

    @Test
    public void shouldPrintMethodNameWhenVerifyingAtLeastOnce() throws Exception {
        try {
            verify(mock, atLeastOnce()).twoArgumentMethod(1, 2);
            fail();
        } catch (WantedButNotInvoked e) {
            assertThat(e, messageContains("twoArgumentMethod(1, 2)"));
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
                "iMethods.twoArgumentMethod(<any>, 100);";
            assertEquals(expectedMessage, actualMessage);
        }
    }

    @Test
    public void shouldPrintMethodWhenMissingInvocationWithArrayMatcher() {
        mock.oneArray(new boolean[] { true, false, false });

        try {
            verify(mock).oneArray(aryEq(new boolean[] { false, false, false }));
            fail();
        } catch (ArgumentsAreDifferent e) {
            assertThat(e, messageContains("[false, false, false]"));
            assertThat(e, causeMessageContains("[true, false, false]"));
        }
    }

    @Test
    public void shouldPrintMethodWhenMissingInvocationWithVarargMatcher() {
        mock.varargsString(10, "xxx", "yyy", "zzz");

        try {
            verify(mock).varargsString(10, "111", "222", "333");
            fail();
        } catch (ArgumentsAreDifferent e) {
            assertThat(e, messageContains("111"));
            assertThat(e, causeMessageContains("\"xxx\""));
        }
    }

    @Test
    public void shouldPrintMethodWhenMissingInvocationWithMatcher() {
        mock.simpleMethod("foo");

        try {
            verify(mock).simpleMethod(matches("burrito from Exmouth"));
            fail();
        } catch (ArgumentsAreDifferent e) {
            assertThat(e, messageContains("matches(\"burrito from Exmouth\")"));
            assertThat(e, causeMessageContains("\"foo\""));
        }
    }

    @Test
    public void shouldPrintNullArguments() throws Exception {
        mock.simpleMethod(null, (Integer) null);
        try {
            verify(mock).simpleMethod("test");
            fail();
        } catch (ArgumentsAreDifferent e) {
            assertThat(e, causeMessageContains("simpleMethod(null, null);"));
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
    
    @Test
    public void shouldShowRightActualMethod() throws Exception {
        mock.simpleMethod(9191);
        mock.simpleMethod("foo");
    
        try {
            verify(mock).simpleMethod("bar");
            fail();
        } catch (ArgumentsAreDifferent e) {
            assertThat(e, messageContains("bar"));
            assertThat(e, causeMessageContains("foo"));
        }
    }

    @Mock private IMethods iHavefunkyName;
    
    @Test
    public void shouldPrintFieldNameWhenAnnotationsUsed() throws Exception {
        iHavefunkyName.simpleMethod(10);
    
        try {
            verify(iHavefunkyName).simpleMethod(20);
            fail();
        } catch (ArgumentsAreDifferent e) {
            assertThat(e, messageContains("iHavefunkyName.simpleMethod(20)"));
            assertThat(e, causeMessageContains("iHavefunkyName.simpleMethod(10)"));
        }
    }

    @Mock private IMethods veeeeeeeeeeeeeeeeeeeeeeeerylongNameMock; 
    
    @Test
    public void shouldNeverBreakMethodStringWhenNoArgsInMethod() throws Exception {
        try {
            verify(veeeeeeeeeeeeeeeeeeeeeeeerylongNameMock).simpleMethod();
            fail();
        } catch(WantedButNotInvoked e) {
            assertThat(e, messageContains("veeeeeeeeeeeeeeeeeeeeeeeerylongNameMock.simpleMethod()"));
        }
    }
}