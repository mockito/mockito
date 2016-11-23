package org.mockitoutil;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public class SafeJUnitRuleTest {

    SafeJUnitRule rule = new SafeJUnitRule(mock(MethodRule.class));

    @Test public void happy_path_no_exception() throws Throwable {
        //when
        rule.apply(new Statement() {
            public void evaluate() throws Throwable {
                //all good
            }
        }, mock(FrameworkMethod.class), this).evaluate();

        //then
        assertNull(rule.getReportedThrowable());
    }

    @Test public void rule_threw_exception() throws Throwable {
        //expect
        rule.expectThrowable(AssertionError.class, "x");

        //when
        rule.apply(new Statement() {
            public void evaluate() throws Throwable {
                throw new AssertionError("x");
            }
        }, mock(FrameworkMethod.class), this).evaluate();

        //then
        assertEquals(rule.getReportedThrowable().getMessage(), "x");
    }

    @Test public void expected_exception_but_no_exception() throws Throwable {
        //expect
        rule.expectThrowable(AssertionError.class, "x");

        //when
        try {
            rule.apply(new Statement() {
                public void evaluate() throws Throwable {
                    //all good
                }
            }, mock(FrameworkMethod.class), this).evaluate();
            fail();

        //then
        } catch (SafeJUnitRule.ExpectedThrowableNotReported t) {
            //yup, expected
        }
    }

    @Test public void expected_exception_message_did_not_match() throws Throwable {
        //expect
        rule.expectThrowable(AssertionError.class, "FOO");

        //when
        try {
            rule.apply(new Statement() {
                public void evaluate() throws Throwable {
                    throw new AssertionError("BAR");
                }
            }, mock(FrameworkMethod.class), this).evaluate();
            fail();
        } catch (AssertionError throwable) {
            Assertions.assertThat(throwable).hasMessageContaining("Expecting message");
        }
    }

    @Test public void expected_exception_type_did_not_match() throws Throwable {
        //expect
        rule.expectThrowable(AssertionError.class, "x");

        //when
        try {
            rule.apply(new Statement() {
                public void evaluate() throws Throwable {
                    throw new RuntimeException("x");
                }
            }, mock(FrameworkMethod.class), this).evaluate();
            fail();
        } catch (AssertionError throwable) {
            Assertions.assertThat(throwable).hasMessageContaining("but was instance of");
        }
    }

    @Test public void expected_exception_assert_did_not_match() throws Throwable {
        //expect
        rule.expectThrowable(new SafeJUnitRule.ThrowableAssert() {
            public void doAssert(Throwable throwable) {
                throw new AssertionError("x");
            }
        });

        //when
        try {
            rule.apply(new Statement() {
                public void evaluate() throws Throwable {
                    throw new RuntimeException();
                }
            }, mock(FrameworkMethod.class), this).evaluate();
            fail();
        } catch (AssertionError throwable) {
            assertEquals(throwable.getMessage(), "x");
        }
    }
}