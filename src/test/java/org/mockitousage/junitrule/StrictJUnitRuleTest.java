package org.mockitousage.junitrule;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.UnfinishedVerificationException;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;
import org.mockito.internal.junit.JUnitRule;
import org.mockito.junit.MockitoJUnit;
import org.mockitousage.IMethods;
import org.mockitoutil.SafeJUnitRule;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockitoutil.TestBase.filterLineNo;

public class StrictJUnitRuleTest {

    @Rule public SafeJUnitRule rule = new SafeJUnitRule(((JUnitRule) MockitoJUnit.rule()).strictStubs());

    @Mock IMethods mock;
    @Mock IMethods mock2;

    @Test public void ok_when_no_stubbings() throws Throwable {
        mock.simpleMethod();
        verify(mock).simpleMethod();
    }

    @Test public void ok_when_all_stubbings_used() throws Throwable {
        given(mock.simpleMethod(10)).willReturn("foo");
        mock.simpleMethod(10);
    }

    @Test public void ok_when_used_and_mismatched_argument() throws Throwable {
        given(mock.simpleMethod(10)).willReturn("foo");
        mock.simpleMethod(10);
        mock.simpleMethod(15);
    }

    @Test public void fails_when_unused_stubbings() throws Throwable {
        //expect
        rule.expectThrowable(UnnecessaryStubbingException.class);

        //when
        given(mock.simpleMethod(10)).willReturn("foo");
        mock2.simpleMethod(10);
    }

    @Test public void test_failure_trumps_unused_stubbings() throws Throwable {
        //expect
        rule.expectThrowable(AssertionError.class, "x");

        //when
        given(mock.simpleMethod(10)).willReturn("foo");
        mock.otherMethod();

        throw new AssertionError("x");
    }

    @Test public void fails_fast_when_stubbing_invoked_with_different_argument() throws Throwable {
        //expect
        rule.expectThrowable(AssertionError.class, "Argument mismatch:\n" +
                " - stubbing: mock.simpleMethod(10);\n" +
                " - actual: mock.simpleMethod(15);");

        //when stubbing in the test code:
        given(mock.simpleMethod(10)).willReturn("foo");

        //and invocation in the code under test uses different argument and should fail immediately
        //this helps with debugging and is essential for Mockito strictness
        mock.simpleMethod(15);
    }

    @Test public void verify_no_more_interactions_ignores_stubs() throws Throwable {
        //when stubbing in test:
        given(mock.simpleMethod(10)).willReturn("foo");

        //and code under test does:
        mock.simpleMethod(10); //implicitly verifies the stubbing
        mock.otherMethod();

        //and in test we:
        verify(mock).otherMethod();
        verifyNoMoreInteractions(mock);
    }

    @Test public void unused_stubs_with_multiple_mocks() throws Throwable {
        //expect
        rule.expectThrowable(new SafeJUnitRule.ThrowableAssert() {
            public void doAssert(Throwable throwable) {
                assertEquals(filterLineNo("\n" +
                        "Unnecessary stubbings detected.\n" +
                        "Clean & maintainable test code requires zero unnecessary code.\n" +
                        "Following stubbings are unnecessary (click to navigate to relevant line of code):\n" +
                        "  1. -> at org.mockitousage.junitrule.StrictJUnitRuleTest.unused_stubs_with_multiple_mocks(StrictJUnitRuleTest.java:0)\n" +
                        "  2. -> at org.mockitousage.junitrule.StrictJUnitRuleTest.unused_stubs_with_multiple_mocks(StrictJUnitRuleTest.java:0)\n" +
                        "Please remove unnecessary stubbings or use 'silent' option. More info: javadoc for UnnecessaryStubbingException class."), filterLineNo(throwable.getMessage()));
            }
        });

        //when test has
        given(mock.simpleMethod(10)).willReturn("foo");
        given(mock2.simpleMethod(20)).willReturn("foo");

        //and code has
        mock.otherMethod();
        mock2.booleanObjectReturningMethod();
    }

    @Test public void rule_validates_mockito_usage() throws Throwable {
        //expect
        rule.expectThrowable(UnfinishedVerificationException.class);

        //when test contains unfinished verification
        verify(mock);
    }
}