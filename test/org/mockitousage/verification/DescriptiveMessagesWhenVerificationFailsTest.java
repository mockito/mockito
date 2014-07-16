/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.verification;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.NeverWantedButInvoked;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class DescriptiveMessagesWhenVerificationFailsTest extends TestBase {

    private IMethods mock;

    @Before
    public void setup() {
        mock = Mockito.mock(IMethods.class);
    }

    @Test
    public void should_print_method_name() {
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
    public void should_print_method_name_and_arguments() {
        try {
            verify(mock).threeArgumentMethod(12, new Foo(), "xx");
            fail();
        } catch (WantedButNotInvoked e) {
            assertContains("iMethods.threeArgumentMethod(12, foo, \"xx\")", e.getMessage());
        }
    }

    @Test
    public void should_print_actual_and_wanted_in_line() {
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
    public void should_print_actual_and_wanted_in_multiple_lines() {
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
    public void should_print_actual_and_wanted_when_actual_method_name_and_wanted_method_name_are_the_same() {
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
    public void should_print_actual_and_unverified_wanted_when_the_difference_is_about_arguments() {
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
    public void should_print_first_unexpected_invocation() {
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
                    "But found this interaction on mock '" + mock + "':" +
                    "\n" +
                    "-> at";
            assertContains(expectedCause, e.getMessage());
        }
    }

    @Test
    public void should_print_first_unexpected_invocation_when_verifying_zero_interactions() {
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
                "But found this interaction on mock '" + mock + "':" +
                "\n" +
                "-> at";

            assertContains(expectedCause, e.getMessage());
        }
    }

    @Test
    public void should_print_method_name_when_verifying_at_least_once() throws Exception {
        try {
            verify(mock, atLeastOnce()).twoArgumentMethod(1, 2);
            fail();
        } catch (WantedButNotInvoked e) {
            assertContains("twoArgumentMethod(1, 2)", e.getMessage());
        }
    }

    @Test
    public void should_print_method_when_matcher_used() throws Exception {
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
    public void should_print_method_when_missing_invocation_with_array_matcher() {
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
    public void should_print_method_when_missing_invocation_with_vararg_matcher() {
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
    public void should_print_method_when_missing_invocation_with_matcher() {
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
    public void should_print_null_arguments() throws Exception {
        mock.simpleMethod(null, (Integer) null);
        try {
            verify(mock).simpleMethod("test");
            fail();
        } catch (ArgumentsAreDifferent e) {
            assertContains("simpleMethod(null, null);", e.getMessage());
        }
    }
    
    @Test
    public void should_say_never_wanted_but_invoked() throws Exception {
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
    public void should_show_right_actual_method() throws Exception {
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
    public void should_print_field_name_when_annotations_used() throws Exception {
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
    public void should_print_interactions_on_mock_when_ordinary_verification_fail() throws Exception {
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
    public void should_never_break_method_string_when_no_args_in_method() throws Exception {
        try {
            verify(veeeeeeeeeeeeeeeeeeeeeeeerylongNameMock).simpleMethod();
            fail();
        } catch(WantedButNotInvoked e) {
            assertContains("veeeeeeeeeeeeeeeeeeeeeeeerylongNameMock.simpleMethod()", e.getMessage());
        }
    }

    @Test
    public void should_print_method_name_and_arguments_of_other_interactions_with_different_methods() throws Exception {
        try {
            mock.arrayMethod(new String[] {"a", "b", "c"});
            mock.forByte((byte) 25);

            verify(mock).threeArgumentMethod(12, new Foo(), "xx");
            fail();
        } catch (WantedButNotInvoked e) {
            System.out.println(e);
            assertContains("iMethods.threeArgumentMethod(12, foo, \"xx\")", e.getMessage());
            assertContains("iMethods.arrayMethod([\"a\", \"b\", \"c\"])", e.getMessage());
            assertContains("iMethods.forByte(25)", e.getMessage());
        }
    }

    @Test
    @Ignore("issue 380 related")
    public void should_print_method_name_and_arguments_of_other_interactions_of_same_method() throws Exception {
        try {
            mock.forByte((byte) 25);
            mock.forByte((byte) 12);

            verify(mock).forByte((byte) 42);
            fail();
        } catch (WantedButNotInvoked e) {
            System.out.println(e);
            assertContains("iMethods.forByte(42)", e.getMessage());
            assertContains("iMethods.forByte(25)", e.getMessage());
            assertContains("iMethods.forByte(12)", e.getMessage());
        }
    }

    @Test
    @Ignore("issue 380 related")
    public void test1() {
        AnInterface m = Mockito.mock(AnInterface.class);

        for (int i = 1; i <= 2; i++) {
            m.foo(i);
        }

        verify(m).foo(1);
        verify(m).foo(2);
        verify(m).foo(3); // XXX: doesn't mention the parameters of foo(1) and foo(2)
        verify(m).foo(4);
    }

    @Test
    @Ignore("issue 380 related")
    public void test2() {
        AnInterface m = Mockito.mock(AnInterface.class);

        for (int i = 1; i <= 4; i++) {
            m.foo(i);
        }

        verify(m).foo(1);
        verify(m).foo(2);
        verify(m).foo(5); // XXX: doesn't mention foo(4) at all
    }

    public interface AnInterface {
        void foo(int i);
    }
    
}