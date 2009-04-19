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
                    "iMethods.simpleMethod();" +
                    "\n" +
                    "-> at";
            assertContains(expectedMessage, actualMessage);
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
            assertContains("iMethods.threeArgumentMethod(12, foo, \"xx\")", e.getMessage());
        }
    }

    @Test
    public void shouldPrintActualAndWantedInLine() {
        mock.varargs(1, 2);

        try {
            verify(mock).varargs(1, 1000);
            fail();
        } catch (ArgumentsAreDifferent e) {
            String wanted =
                    "\n" +
                    "Argument(s) are different! Wanted:" +
                    "\n" +
                    "iMethods.varargs(1, 1000);";

            assertContains(wanted, e.getMessage());
            
            String actual = 
                    "\n" +
                    "Actual invocation has different arguments:" +
                    "\n" +
                    "iMethods.varargs(1, 2);";

            assertContains(actual, e.getMessage());
        }
    }
    
    @Test
    public void shouldPrintActualAndWantedInMultipleLines() {
        mock.varargs("this is very long string", "this is another very long string");

        try {
            verify(mock).varargs("x", "y", "z");
            fail();
        } catch (ArgumentsAreDifferent e) {
            String wanted =
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

            assertContains(wanted, e.getMessage());

            String actual =
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

            assertContains(actual, e.getMessage());
        }
    }

    @Test
    public void shouldPrintActualAndWantedWhenActualMethodNameAndWantedMethodNameAreTheSame() {
        mock.simpleMethod();

        try {
            verify(mock).simpleMethod(10);
            fail();
        } catch (ArgumentsAreDifferent e) {
            assertContains("simpleMethod(10)", e.getMessage());
            assertContains("simpleMethod()", e.getMessage());
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
            assertContains("(2, 1000)", e.getMessage());
            assertContains("(2, 2)", e.getMessage());
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
                    "No interactions wanted here:" +
                    "\n" +
                    "-> at";
            assertContains(expectedMessage, e.getMessage());

            String expectedCause =
                    "\n" +
                    "But found this interaction:" +
                    "\n" +
                    "-> at";
            assertContains(expectedCause, e.getMessage());
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
                    "No interactions wanted here:" +
                    "\n" +
                    "-> at";

            assertContains(expected, e.getMessage());

            String expectedCause =
                "\n" +
                "But found this interaction:" +
                "\n" +
                "-> at";

            assertContains(expectedCause, e.getMessage());
        }
    }

    @Test
    public void shouldPrintMethodNameWhenVerifyingAtLeastOnce() throws Exception {
        try {
            verify(mock, atLeastOnce()).twoArgumentMethod(1, 2);
            fail();
        } catch (WantedButNotInvoked e) {
            assertContains("twoArgumentMethod(1, 2)", e.getMessage());
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
            assertContains(expectedMessage, actualMessage);
        }
    }

    @Test
    public void shouldPrintMethodWhenMissingInvocationWithArrayMatcher() {
        mock.oneArray(new boolean[] { true, false, false });

        try {
            verify(mock).oneArray(aryEq(new boolean[] { false, false, false }));
            fail();
        } catch (ArgumentsAreDifferent e) {
            assertContains("[false, false, false]", e.getMessage());
            assertContains("[true, false, false]", e.getMessage());
        }
    }

    @Test
    public void shouldPrintMethodWhenMissingInvocationWithVarargMatcher() {
        mock.varargsString(10, "xxx", "yyy", "zzz");

        try {
            verify(mock).varargsString(10, "111", "222", "333");
            fail();
        } catch (ArgumentsAreDifferent e) {
            assertContains("111", e.getMessage());
            assertContains("\"xxx\"", e.getMessage());
        }
    }

    @Test
    public void shouldPrintMethodWhenMissingInvocationWithMatcher() {
        mock.simpleMethod("foo");

        try {
            verify(mock).simpleMethod(matches("burrito from Exmouth"));
            fail();
        } catch (ArgumentsAreDifferent e) {
            assertContains("matches(\"burrito from Exmouth\")", e.getMessage());
            assertContains("\"foo\"", e.getMessage());
        }
    }

    @Test
    public void shouldPrintNullArguments() throws Exception {
        mock.simpleMethod(null, (Integer) null);
        try {
            verify(mock).simpleMethod("test");
            fail();
        } catch (ArgumentsAreDifferent e) {
            assertContains("simpleMethod(null, null);", e.getMessage());
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
            assertContains("Never wanted here:", e.getMessage());
            assertContains("But invoked here:", e.getMessage());
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
            assertContains("bar", e.getMessage());
            assertContains("foo", e.getMessage());
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
            assertContains("iHavefunkyName.simpleMethod(20)", e.getMessage());
            assertContains("iHavefunkyName.simpleMethod(10)", e.getMessage());
        }
    }
    
    @Test
    public void shouldPrintInteractionsOnMockWhenOrdinaryVerificationFail() throws Exception {
        mock.otherMethod();
        mock.booleanReturningMethod();
        
        try {
            verify(mock).simpleMethod();
            fail();
        } catch (WantedButNotInvoked e) {
//            assertContains("")
        }
    }

    @Mock private IMethods veeeeeeeeeeeeeeeeeeeeeeeerylongNameMock; 
    
    @Test
    public void shouldNeverBreakMethodStringWhenNoArgsInMethod() throws Exception {
        try {
            verify(veeeeeeeeeeeeeeeeeeeeeeeerylongNameMock).simpleMethod();
            fail();
        } catch(WantedButNotInvoked e) {
            assertContains("veeeeeeeeeeeeeeeeeeeeeeeerylongNameMock.simpleMethod()", e.getMessage());
        }
    }
}