package org.mockitousage.junitrule;

import org.junit.After;
import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.internal.junit.JUnitRule;
import org.mockito.internal.util.SimpleMockitoLogger;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StubbingWarningsJUnitRuleTest extends TestBase {

    private SimpleMockitoLogger logger = new SimpleMockitoLogger();
    private JUnitRule jUnitRule = new JUnitRule(logger, false);
    private FrameworkMethod dummy = mock(FrameworkMethod.class);

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
            }, dummy, new DummyTestCase()).evaluate();

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
            }, dummy, new DummyTestCase()).evaluate();

            //then
            fail();
        } catch (AssertionError e) {
            assertEquals("x", e.getMessage());
            assertEquals(
                "[MockitoHint] DummyTestCase.null (see javadoc for MockitoHint):\n" +
                "[MockitoHint] 1. Unused... -> at org.mockitousage.junitrule.StubbingWarningsJUnitRuleTest.declareStubbingWithArg(StubbingWarningsJUnitRuleTest.java:0)\n" +
                "[MockitoHint]  ...args ok? -> at org.mockitousage.junitrule.StubbingWarningsJUnitRuleTest.useStubbingWithArg(StubbingWarningsJUnitRuleTest.java:0)\n",
                    filterLineNo(logger.getLoggedInfo()));
        }
    }

    @Test
    public void no_stubbing_arg_mismatch_when_no_mismatch_on_fail() throws Throwable {
        try {
            //when
            jUnitRule.apply(new Statement() {
                public void evaluate() throws Throwable {
                    IMethods mock = mock(IMethods.class);
                    declareStubbingWithArg(mock, "a");
                    useStubbingWithArg(mock, "a");
                    throw new AssertionError("x");
                }
            }, dummy, new DummyTestCase()).evaluate();

            //then
            fail();
        } catch (AssertionError e) {
            assertEquals("x", e.getMessage());
            assertTrue(logger.isEmpty());
        }
    }

    @Test
    public void no_stubbing_warning_on_pass() throws Throwable {
        jUnitRule.apply(new Statement() {
            public void evaluate() throws Throwable {
                IMethods mock = mock(IMethods.class);
                declareStubbingWithArg(mock, "a");
                useStubbingWithArg(mock, "a");
            }
        }, dummy, new DummyTestCase()).evaluate();

        assertTrue(logger.isEmpty());
    }

    @Test
    public void multiple_stubbing_arg_mismatch_on_failure() throws Throwable {
        try {
            //when
            jUnitRule.apply(new Statement() {
                public void evaluate() throws Throwable {
                    IMethods mock = mock(IMethods.class);

                    declareStubbingWithArg(mock, "a");
                    declareStubbingWithArg(mock, "b");

                    useStubbingWithArg(mock, "c");
                    useStubbingWithArg(mock, "d");

                    throw new AssertionError("x");
                }
            }, dummy, new DummyTestCase()).evaluate();

            //then
            fail();
        } catch (AssertionError e) {
            assertEquals("x", e.getMessage());
            assertEquals(
                "[MockitoHint] DummyTestCase.null (see javadoc for MockitoHint):\n" +
                "[MockitoHint] 1. Unused... -> at org.mockitousage.junitrule.StubbingWarningsJUnitRuleTest.declareStubbingWithArg(StubbingWarningsJUnitRuleTest.java:0)\n" +
                "[MockitoHint]  ...args ok? -> at org.mockitousage.junitrule.StubbingWarningsJUnitRuleTest.useStubbingWithArg(StubbingWarningsJUnitRuleTest.java:0)\n" +
                "[MockitoHint]  ...args ok? -> at org.mockitousage.junitrule.StubbingWarningsJUnitRuleTest.useStubbingWithArg(StubbingWarningsJUnitRuleTest.java:0)\n" +
                "[MockitoHint] 2. Unused... -> at org.mockitousage.junitrule.StubbingWarningsJUnitRuleTest.declareStubbingWithArg(StubbingWarningsJUnitRuleTest.java:0)\n" +
                "[MockitoHint]  ...args ok? -> at org.mockitousage.junitrule.StubbingWarningsJUnitRuleTest.useStubbingWithArg(StubbingWarningsJUnitRuleTest.java:0)\n" +
                "[MockitoHint]  ...args ok? -> at org.mockitousage.junitrule.StubbingWarningsJUnitRuleTest.useStubbingWithArg(StubbingWarningsJUnitRuleTest.java:0)\n",
                    filterLineNo(logger.getLoggedInfo()));
        }
    }

    @Test
    public void reports_only_mismatching_stubs() throws Throwable {
        try {
            //when
            jUnitRule.apply(new Statement() {
                public void evaluate() throws Throwable {
                    IMethods mock = mock(IMethods.class);

                    declareStubbingWithArg(mock, "a"); // <-- used
                    declareStubbingWithArg(mock, "b"); // <-- unused

                    useStubbingWithArg(mock, "a");
                    useStubbingWithArg(mock, "d"); // <-- arg mismatch

                    throw new AssertionError("x");
                }
            }, dummy, new DummyTestCase()).evaluate();

            //then
            fail();
        } catch (AssertionError e) {
            assertEquals("x", e.getMessage());
            assertEquals(
                "[MockitoHint] DummyTestCase.null (see javadoc for MockitoHint):\n" +
                "[MockitoHint] 1. Unused... -> at org.mockitousage.junitrule.StubbingWarningsJUnitRuleTest.declareStubbingWithArg(StubbingWarningsJUnitRuleTest.java:0)\n" +
                "[MockitoHint]  ...args ok? -> at org.mockitousage.junitrule.StubbingWarningsJUnitRuleTest.useStubbingWithArg(StubbingWarningsJUnitRuleTest.java:0)\n",
                    filterLineNo(logger.getLoggedInfo()));
        }
    }

    @Test
    public void no_mismatch_when_stub_was_used() throws Throwable {
        try {
            //when
            jUnitRule.apply(new Statement() {
                public void evaluate() throws Throwable {
                    IMethods mock = mock(IMethods.class);

                    declareStubbingWithArg(mock, "a");

                    useStubbingWithArg(mock, "a");
                    useStubbingWithArg(mock, "d"); // <-- arg mismatch, but the stub was already used

                    throw new AssertionError("x");
                }
            }, dummy, new DummyTestCase()).evaluate();

            //then
            fail();
        } catch (AssertionError e) {
            assertEquals("x", e.getMessage());
            assertEquals("", logger.getLoggedInfo());
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
        }, dummy, new DummyTestCase()).evaluate();

        //expect
        assertEquals(
            "[MockitoHint] DummyTestCase.null (see javadoc for MockitoHint):\n" +
            "[MockitoHint] 1. Unused -> at org.mockitousage.junitrule.StubbingWarningsJUnitRuleTest.declareStubbingWithArg(StubbingWarningsJUnitRuleTest.java:0)\n",
                filterLineNo(logger.getLoggedInfo()));
    }

    @Test
    public void warns_about_unused_stubs_when_passed() throws Throwable {
        jUnitRule.apply(new Statement() {
            public void evaluate() throws Throwable {
                IMethods mock = mock(IMethods.class);
                declareStubbing(mock);
            }
        }, dummy, new DummyTestCase()).evaluate();

        assertEquals(
            "[MockitoHint] DummyTestCase.null (see javadoc for MockitoHint):\n" +
            "[MockitoHint] 1. Unused -> at org.mockitousage.junitrule.StubbingWarningsJUnitRuleTest.declareStubbing(StubbingWarningsJUnitRuleTest.java:0)\n",
                filterLineNo(logger.getLoggedInfo()));
    }

    @Test
    public void no_warnings_when_silent() throws Throwable {
        jUnitRule = new JUnitRule(logger, false).silent();
        jUnitRule.apply(new Statement() {
            public void evaluate() throws Throwable {
                IMethods mock = mock(IMethods.class);
                declareStubbing(mock);
            }
        }, dummy, new DummyTestCase()).evaluate();

        assertTrue(logger.isEmpty());
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

    public static class DummyTestCase {
        @Mock private IMethods mock;
    }
}