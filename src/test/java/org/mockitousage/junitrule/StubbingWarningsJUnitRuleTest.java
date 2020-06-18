/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.junitrule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockitoutil.TestBase.filterLineNo;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.junit.JUnitRule;
import org.mockito.internal.util.SimpleMockitoLogger;
import org.mockito.quality.Strictness;
import org.mockitousage.IMethods;
import org.mockitoutil.SafeJUnitRule;

public class StubbingWarningsJUnitRuleTest {

    private SimpleMockitoLogger logger = new SimpleMockitoLogger();
    @Rule public SafeJUnitRule rule = new SafeJUnitRule(new JUnitRule(logger, Strictness.WARN));
    @Mock IMethods mock;

    @Test
    public void no_unused_stubs_reported_on_failure() throws Throwable {
        // expect
        rule.expectFailure(
                new SafeJUnitRule.FailureAssert() {
                    public void doAssert(Throwable t) {
                        assertEquals("x", t.getMessage());
                        assertTrue(logger.getLoggedInfo().isEmpty());
                    }
                });

        // when
        declareStubbing(mock);
        throw new AssertionError("x");
    }

    @Test
    public void stubbing_arg_mismatch_on_failure() throws Throwable {
        // expect
        rule.expectFailure(
                new SafeJUnitRule.FailureAssert() {
                    public void doAssert(Throwable t) {
                        assertEquals("x", t.getMessage());
                        assertEquals(
                                "[MockitoHint] StubbingWarningsJUnitRuleTest.stubbing_arg_mismatch_on_failure (see javadoc for MockitoHint):\n"
                                        + "[MockitoHint] 1. Unused... -> at org.mockitousage.junitrule.StubbingWarningsJUnitRuleTest.declareStubbingWithArg(StubbingWarningsJUnitRuleTest.java:0)\n"
                                        + "[MockitoHint]  ...args ok? -> at org.mockitousage.junitrule.StubbingWarningsJUnitRuleTest.useStubbingWithArg(StubbingWarningsJUnitRuleTest.java:0)\n",
                                filterLineNo(logger.getLoggedInfo()));
                    }
                });

        // when
        declareStubbingWithArg(mock, "a");
        useStubbingWithArg(mock, "b");
        throw new AssertionError("x");
    }

    @Test
    public void no_stubbing_arg_mismatch_when_no_mismatch_on_fail() throws Throwable {
        // expect
        rule.expectFailure(
                new SafeJUnitRule.FailureAssert() {
                    public void doAssert(Throwable t) {
                        assertEquals("x", t.getMessage());
                        assertTrue(logger.getLoggedInfo().isEmpty());
                    }
                });

        // when
        declareStubbingWithArg(mock, "a");
        useStubbingWithArg(mock, "a");
        throw new AssertionError("x");
    }

    @Test
    public void no_stubbing_warning_on_pass() throws Throwable {
        // expect
        rule.expectSuccess(
                new Runnable() {
                    public void run() {
                        assertTrue(logger.isEmpty());
                    }
                });

        // when
        declareStubbingWithArg(mock, "a");
        useStubbingWithArg(mock, "a");
    }

    @Test
    public void multiple_stubbing_arg_mismatch_on_failure() throws Throwable {
        // expect
        rule.expectFailure(
                new SafeJUnitRule.FailureAssert() {
                    public void doAssert(Throwable t) {
                        assertEquals("x", t.getMessage());
                        assertEquals(
                                "[MockitoHint] StubbingWarningsJUnitRuleTest.multiple_stubbing_arg_mismatch_on_failure (see javadoc for MockitoHint):\n"
                                        + "[MockitoHint] 1. Unused... -> at org.mockitousage.junitrule.StubbingWarningsJUnitRuleTest.declareStubbingWithArg(StubbingWarningsJUnitRuleTest.java:0)\n"
                                        + "[MockitoHint]  ...args ok? -> at org.mockitousage.junitrule.StubbingWarningsJUnitRuleTest.useStubbingWithArg(StubbingWarningsJUnitRuleTest.java:0)\n"
                                        + "[MockitoHint]  ...args ok? -> at org.mockitousage.junitrule.StubbingWarningsJUnitRuleTest.useStubbingWithArg(StubbingWarningsJUnitRuleTest.java:0)\n"
                                        + "[MockitoHint] 2. Unused... -> at org.mockitousage.junitrule.StubbingWarningsJUnitRuleTest.declareStubbingWithArg(StubbingWarningsJUnitRuleTest.java:0)\n"
                                        + "[MockitoHint]  ...args ok? -> at org.mockitousage.junitrule.StubbingWarningsJUnitRuleTest.useStubbingWithArg(StubbingWarningsJUnitRuleTest.java:0)\n"
                                        + "[MockitoHint]  ...args ok? -> at org.mockitousage.junitrule.StubbingWarningsJUnitRuleTest.useStubbingWithArg(StubbingWarningsJUnitRuleTest.java:0)\n",
                                filterLineNo(logger.getLoggedInfo()));
                    }
                });

        // when
        declareStubbingWithArg(mock, "a");
        declareStubbingWithArg(mock, "b");

        useStubbingWithArg(mock, "c");
        useStubbingWithArg(mock, "d");

        throw new AssertionError("x");
    }

    @Test
    public void reports_only_mismatching_stubs() throws Throwable {
        // expect
        rule.expectFailure(
                new SafeJUnitRule.FailureAssert() {
                    public void doAssert(Throwable t) {
                        assertEquals("x", t.getMessage());
                        assertEquals(
                                "[MockitoHint] StubbingWarningsJUnitRuleTest.reports_only_mismatching_stubs (see javadoc for MockitoHint):\n"
                                        + "[MockitoHint] 1. Unused... -> at org.mockitousage.junitrule.StubbingWarningsJUnitRuleTest.declareStubbingWithArg(StubbingWarningsJUnitRuleTest.java:0)\n"
                                        + "[MockitoHint]  ...args ok? -> at org.mockitousage.junitrule.StubbingWarningsJUnitRuleTest.useStubbingWithArg(StubbingWarningsJUnitRuleTest.java:0)\n",
                                filterLineNo(logger.getLoggedInfo()));
                    }
                });

        // when
        declareStubbingWithArg(mock, "a"); // <-- used
        declareStubbingWithArg(mock, "b"); // <-- unused

        useStubbingWithArg(mock, "a");
        useStubbingWithArg(mock, "d"); // <-- arg mismatch

        throw new AssertionError("x");
    }

    @Test
    public void no_mismatch_when_stub_was_used() throws Throwable {
        // expect
        rule.expectFailure(
                new SafeJUnitRule.FailureAssert() {
                    public void doAssert(Throwable t) {
                        assertEquals("x", t.getMessage());
                        assertTrue(logger.getLoggedInfo().isEmpty());
                    }
                });

        // when
        declareStubbingWithArg(mock, "a");

        useStubbingWithArg(mock, "a");
        useStubbingWithArg(mock, "d"); // <-- arg mismatch, but the stub was already used

        throw new AssertionError("x");
    }

    @Test
    public void no_stubbing_arg_mismatch_on_pass() throws Throwable {
        // expect
        rule.expectSuccess(
                new Runnable() {
                    public void run() {
                        assertEquals(
                                "[MockitoHint] StubbingWarningsJUnitRuleTest.no_stubbing_arg_mismatch_on_pass (see javadoc for MockitoHint):\n"
                                        + "[MockitoHint] 1. Unused -> at org.mockitousage.junitrule.StubbingWarningsJUnitRuleTest.declareStubbingWithArg(StubbingWarningsJUnitRuleTest.java:0)\n",
                                filterLineNo(logger.getLoggedInfo()));
                    }
                });

        // when
        declareStubbingWithArg(mock, "a");
        useStubbingWithArg(mock, "b");
    }

    @Test
    public void warns_about_unused_stubs_when_passed() throws Throwable {
        // expect
        rule.expectSuccess(
                new Runnable() {
                    public void run() {
                        assertEquals(
                                "[MockitoHint] StubbingWarningsJUnitRuleTest.warns_about_unused_stubs_when_passed (see javadoc for MockitoHint):\n"
                                        + "[MockitoHint] 1. Unused -> at org.mockitousage.junitrule.StubbingWarningsJUnitRuleTest.declareStubbing(StubbingWarningsJUnitRuleTest.java:0)\n",
                                filterLineNo(logger.getLoggedInfo()));
                    }
                });

        // when
        declareStubbing(mock);
    }

    private static void declareStubbingWithArg(IMethods mock, String arg) {
        when(mock.simpleMethod(arg)).thenReturn("bar");
    }

    private static void declareStubbing(IMethods mock) {
        when(mock.simpleMethod("foo")).thenReturn("bar");
    }

    private void useStubbingWithArg(IMethods mock, String arg) {
        mock.simpleMethod(arg);
    }
}
