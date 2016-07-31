package org.mockitousage.junitrule;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.junit.JUnitRule;
import org.mockito.internal.util.SimpleMockitoLogger;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class StubbingWarningsJUnitRuleTest extends TestBase {

    private SimpleMockitoLogger logger = new SimpleMockitoLogger();
    private JUnitRule jUnitRule = new JUnitRule(logger);

    @After public void after() {
        //so that the validate framework usage exceptions do not collide with the tests here
        resetState();
    }

    @Test
    public void no_unused_stubs_reported_on_failure() throws Throwable {
        try {
            //when
            jUnitRule.apply(new Statement() {
                public void evaluate() throws Throwable {
                    IMethods mock = mock(IMethods.class);
                    declareStubbing(mock);
                    throw new AssertionError("x");
                }
            },null, new DummyTestCase()).evaluate();

            //then
            fail();
        } catch (AssertionError e) {
            assertEquals("x", e.getMessage());
            assertTrue(logger.getLoggedInfo().isEmpty());
        }
    }

    @Test
    public void stubbing_arg_mismatch_on_failure() throws Throwable {
        try {
            //when
            jUnitRule.apply(new Statement() {
                public void evaluate() throws Throwable {
                    IMethods mock = mock(IMethods.class);
                    declareStubbingWithArg(mock, "a");
                    useStubbingWithArg(mock, "b");
                    throw new AssertionError("x");
                }
            },null, new DummyTestCase()).evaluate();

            //then
            fail();
        } catch (AssertionError e) {
            assertEquals("x", e.getMessage());
            assertEquals(filterLineNo(logger.getLoggedInfo()),
                "[MockitoHint] See javadoc for MockitoHint class.\n" +
                "[MockitoHint] 1. unused stub  -> at org.mockitousage.junitrule.StubbingWarningsJUnitRuleTest.declareStubbingWithArg(StubbingWarningsJUnitRuleTest.java:0)\n" +
                "[MockitoHint]    similar call -> at org.mockitousage.junitrule.StubbingWarningsJUnitRuleTest.useStubbingWithArg(StubbingWarningsJUnitRuleTest.java:0)"
            );
        }
    }

    @Test
    public void no_stubbing_arg_mismatch_on_pass() throws Throwable {
        //given
        jUnitRule.apply(new Statement() {
            public void evaluate() throws Throwable {
                IMethods mock = mock(IMethods.class);
                declareStubbingWithArg(mock, "a");
                useStubbingWithArg(mock, "b");
            }
        },null, new DummyTestCase()).evaluate();

        //expect
        assertTrue(logger.getLoggedInfo().isEmpty());
    }

    // arg_mismatches_when_multiple_similar_stubbings
    // arg_mismatches_reports_unused_stubbings_only_when_multiple_similar_stubbings?
    // arg_mismatches_reports_unused_stubbings_first_when_multiple_similar_stubbings?
    // arg_mismatches_reports_stubbings_orderly_when_multiple_similar_stubbings
    // arg_mismatches_when_similar_stubbings_declared_in_same_line
    // arg_mismatches_when_invocations_triggered_in_same_line
    // arg_mismatches_when_similar_stubbings_was_used_before

    @Test
    @Ignore //work in progress
    public void warns_about_unused_stubs_when_passed() throws Throwable {
        jUnitRule.apply(new Statement() {
            public void evaluate() throws Throwable {
                IMethods mock = mock(IMethods.class);
                declareStubbing(mock);
            }
        },null, new DummyTestCase()).evaluate();

        assertEquals("<TODO>", logger.getLoggedInfo());
    }

    private static void declareStubbingWithArg(IMethods mock, String arg) {
        Mockito.when(mock.simpleMethod(arg)).thenReturn("bar");
    }

    private static void declareStubbing(IMethods mock) {
        Mockito.when(mock.simpleMethod("foo")).thenReturn("bar");
    }

    private void useStubbingWithArg(IMethods mock, String arg) {
        mock.simpleMethod(arg);
    }

    public static class DummyTestCase {
        @Mock private IMethods mock;
    }
}