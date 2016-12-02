/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitoutil;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static java.lang.String.format;

public class RetryRule implements TestRule {
    private final TestRule innerRule;

    public static RetryRule attempts(final int attempts) {
        return new RetryRule(new NumberedAttempts(attempts));
    }

    private RetryRule(TestRule innerRule) {
        this.innerRule = innerRule;
    }

    public Statement apply(final Statement base, final Description description) {
        return innerRule.apply(base, description);
    }

    private static class NumberedAttempts implements TestRule {
        private final int attempts;

        NumberedAttempts(int attempts) {
            assert attempts > 1;
            this.attempts = attempts;
        }

        @Override
        public Statement apply(final Statement base, final Description description) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    for (int remainingAttempts = attempts; remainingAttempts > 0 ; remainingAttempts--) {
                        try {
                            base.evaluate();
                        } catch (Throwable throwable) {
                            if (remainingAttempts < 0) {
                                throw new AssertionError(format("Tried this test + %d times and failed", attempts))
                                        .initCause(throwable);
                            }
                        }
                    }
                }
            };
        }
    }
}
