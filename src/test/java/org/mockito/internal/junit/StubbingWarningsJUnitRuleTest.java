package org.mockito.internal.junit;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.SimpleMockitoLogger;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public class StubbingWarningsJUnitRuleTest extends TestBase {

    private SimpleMockitoLogger logger = new SimpleMockitoLogger();
    private JUnitRule jUnitRule = new JUnitRule(logger);

    @After public void after() {
        //so that the validate framework usage exceptions do not collide with the tests here
        resetState();
    }

    @Test
    @Ignore //work in progress
    public void should_warn_about_stubbing_arg_mismatch_on_failure() throws Throwable {
        try {
            jUnitRule.apply(new Statement() {
                public void evaluate() throws Throwable {
                    IMethods mock = mock(IMethods.class);
                    declareUnusedStub(mock);
                    throw new AssertionError("x");
                }
            },null, new DummyTestCase()).evaluate();
            fail();
        } catch (AssertionError e) {
            assertEquals("x", e.getMessage());
            assertEquals(filterLineNo(logger.getLoggedInfo()),
                "[MockitoHint] 1. unused stubbing -> at org.mockito.internal.junit.JUnitRuleTest.declareUnusedStub(JUnitRuleTest.java:0)"
            );
        }
    }

    // arg_mismatches_when_multiple_similar_stubbings
    // arg_mismatches_when_similar_stubbings_declared_in_same_line
    // arg_mismatches_when_invocations_triggered_in_same_line
    // arg_mismatches_when_similar_stubbings_was_used_before

    @Test
    @Ignore //work in progress
    public void warns_about_unused_stubs_when_passed() throws Throwable {
        jUnitRule.apply(new Statement() {
            public void evaluate() throws Throwable {
                IMethods mock = mock(IMethods.class);
                declareUnusedStub(mock);
            }
        },null, new DummyTestCase()).evaluate();

        assertEquals("<TODO>", logger.getLoggedInfo());
    }

    private static void declareUnusedStub(IMethods mock) {
        Mockito.when(mock.simpleMethod("foo")).thenReturn("bar");
    }

    public static class DummyTestCase {
        @Mock private IMethods mock;
    }
}