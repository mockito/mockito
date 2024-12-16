/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoutil;

import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.Assertions;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * Junit rule for testing exception handling other JUnit rules, like Mockito JUnit rules.
 * Makes it easy to assert on expected exceptions triggered by the rule under test.
 */
public class SafeJUnitRule implements MethodRule {

    private final MethodRule testedRule;
    private FailureAssert failureAssert = null;
    private Runnable successAssert =
            new Runnable() {
                public void run() {}
            };

    /**
     * Wraps rule under test with exception handling so that it is easy to assert on exceptions fired from the tested rule.
     */
    public SafeJUnitRule(MethodRule testedRule) {
        this.testedRule = testedRule;
    }

    public Statement apply(
            final Statement base, final FrameworkMethod method, final Object target) {
        return new Statement() {
            public void evaluate() throws Throwable {
                try {
                    testedRule.apply(base, method, target).evaluate();
                    successAssert.run();
                } catch (Throwable t) {
                    if (failureAssert == null) {
                        throw t;
                    }
                    failureAssert.doAssert(t);
                    return;
                }
                if (failureAssert != null) {
                    // looks like the user expects a throwable but it was not thrown!
                    throw new ExpectedThrowableNotReported(
                            "Expected the tested rule to throw an exception but it did not.");
                }
            }
        };
    }

    /**
     * Expects that _after_ the test, the rule will fire specific exception with specific exception message
     */
    public void expectFailure(
            final Class<? extends Throwable> expected, final String expectedMessage) {
        this.expectFailure(
                new FailureAssert() {
                    public void doAssert(Throwable t) {
                        assertThrowable(t, expected).hasMessage(expectedMessage);
                    }
                });
    }

    /**
     * Expects that _after_ the test, the rule will fire specific exception with specific exception message
     */
    public void expectFailure(final Class<? extends Throwable> expected) {
        this.expectFailure(
                new FailureAssert() {
                    public void doAssert(Throwable t) {
                        assertThrowable(t, expected);
                    }
                });
    }

    private static AbstractThrowableAssert assertThrowable(
            Throwable throwable, Class<? extends Throwable> expected) {
        return Assertions.assertThat(throwable).isInstanceOf(expected);
    }

    /**
     * Expects that _after_ the test, the rule will fire an exception
     */
    public void expectFailure(FailureAssert failureAssert) {
        this.failureAssert = failureAssert;
    }

    /**
     * Expects that _after_ the test, given assert will run
     */
    public void expectSuccess(Runnable successAssert) {
        this.successAssert = successAssert;
    }

    /**
     * Offers a way to assert the throwable triggered by the tested rule
     */
    public interface FailureAssert {
        void doAssert(Throwable t);
    }

    /**
     * Thrown when user expects the tested rule to throw an exception but no exception was thrown
     */
    class ExpectedThrowableNotReported extends Throwable {
        public ExpectedThrowableNotReported(String message) {
            super(message);
        }
    }
}
