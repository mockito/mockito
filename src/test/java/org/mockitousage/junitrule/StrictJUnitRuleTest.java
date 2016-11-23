package org.mockitousage.junitrule;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.UnfinishedVerificationException;
import org.mockito.internal.junit.JUnitRule;
import org.mockito.junit.MockitoJUnit;
import org.mockitousage.IMethods;
import org.mockitoutil.SafeJUnitRule;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class StrictJUnitRuleTest extends TestBase {

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
        //when
        given(mock.simpleMethod(10)).willReturn("foo");
        mock2.simpleMethod(10);

        //then
        String message = rule.getReportedThrowable().getMessage();
        Assertions.assertThat(message).startsWith("Unused stubbings");
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
        //when stubbing in the test code:
        given(mock.simpleMethod(10)).willReturn("foo");

        //and invocation in the code under test uses different argument and should fail immediately
        //this helps with debugging and is essential for Mockito strictness
        mock.simpleMethod(15);

        //then
        Assertions.assertThat(rule.getReportedThrowable()).hasMessageStartingWith("Argument mismatch");
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

        //then no exception is thrown
        assertNull(rule.getReportedThrowable());
    }

    @Test public void unused_stubs_with_multiple_mocks() throws Throwable {
        //test
        given(mock.simpleMethod(10)).willReturn("foo");
        given(mock2.simpleMethod(20)).willReturn("foo");

        //code
        mock.otherMethod();
        mock2.booleanObjectReturningMethod();

        //then
        Assertions.assertThat(rule.getReportedThrowable()).hasMessageStartingWith("Unused stubbings");
    }

    @Test public void rule_validates_mockito_usage() throws Throwable {
        //expect
        rule.expectThrowable(UnfinishedVerificationException.class);

        //when test contains unfinished verification
        verify(mock);
    }
}