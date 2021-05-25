/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoutil;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class SafeJUnitRuleTest {

    MethodRuleStub delegate = new MethodRuleStub();
    SafeJUnitRule rule = new SafeJUnitRule(delegate);

    @Test
    public void happy_path_no_exception() throws Throwable {
        // when
        rule.apply(
                        new Statement() {
                            public void evaluate() throws Throwable {
                                // all good
                            }
                        },
                        mock(FrameworkMethod.class),
                        this)
                .evaluate();

        // then
        assertTrue(delegate.statementEvaluated);
    }

    @Test(expected = IllegalArgumentException.class)
    public void regular_failing_test() throws Throwable {
        // when
        rule.apply(
                        new Statement() {
                            public void evaluate() throws Throwable {
                                throw new IllegalArgumentException();
                            }
                        },
                        mock(FrameworkMethod.class),
                        this)
                .evaluate();
    }

    @Test
    public void rule_threw_exception() throws Throwable {
        // expect
        rule.expectFailure(AssertionError.class, "x");

        // when
        rule.apply(
                        new Statement() {
                            public void evaluate() throws Throwable {
                                throw new AssertionError("x");
                            }
                        },
                        mock(FrameworkMethod.class),
                        this)
                .evaluate();
    }

    @Test
    public void expected_exception_but_no_exception() throws Throwable {
        // expect
        rule.expectFailure(AssertionError.class, "x");

        // when
        try {
            rule.apply(
                            new Statement() {
                                public void evaluate() throws Throwable {
                                    // all good
                                }
                            },
                            mock(FrameworkMethod.class),
                            this)
                    .evaluate();
            fail();

            // then
        } catch (SafeJUnitRule.ExpectedThrowableNotReported t) {
            // yup, expected
        }
    }

    @Test
    public void expected_exception_message_did_not_match() throws Throwable {
        // expect
        rule.expectFailure(AssertionError.class, "FOO");

        // when
        try {
            rule.apply(
                            new Statement() {
                                public void evaluate() throws Throwable {
                                    throw new AssertionError("BAR");
                                }
                            },
                            mock(FrameworkMethod.class),
                            this)
                    .evaluate();
            fail();
        } catch (AssertionError throwable) {
            Assertions.assertThat(throwable).hasMessageContaining("Expecting message");
        }
    }

    @Test
    public void expected_exception_type_did_not_match() throws Throwable {
        // expect
        rule.expectFailure(AssertionError.class, "x");

        // when
        try {
            rule.apply(
                            new Statement() {
                                public void evaluate() throws Throwable {
                                    throw new RuntimeException("x");
                                }
                            },
                            mock(FrameworkMethod.class),
                            this)
                    .evaluate();
            fail();
        } catch (AssertionError throwable) {
            Assertions.assertThat(throwable).hasMessageContaining("but was:");
        }
    }

    @Test
    public void expected_exception_assert_did_not_match() throws Throwable {
        // expect
        rule.expectFailure(
                new SafeJUnitRule.FailureAssert() {
                    public void doAssert(Throwable t) {
                        throw new AssertionError("x");
                    }
                });

        // when
        try {
            rule.apply(
                            new Statement() {
                                public void evaluate() throws Throwable {
                                    throw new RuntimeException();
                                }
                            },
                            mock(FrameworkMethod.class),
                            this)
                    .evaluate();
            fail();
        } catch (AssertionError throwable) {
            assertEquals(throwable.getMessage(), "x");
        }
    }

    private static class MethodRuleStub implements MethodRule {
        private boolean statementEvaluated;

        public Statement apply(final Statement base, FrameworkMethod method, Object target) {
            return new Statement() {
                public void evaluate() throws Throwable {
                    statementEvaluated = true;
                    base.evaluate();
                }
            };
        }
    }
}
