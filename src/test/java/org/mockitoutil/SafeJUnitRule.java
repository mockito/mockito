package org.mockitoutil;

import org.assertj.core.api.Assertions;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * Junit rule for testing exception handling other JUnit rules, like Mockito JUnit rules.
 * Makes it easy to assert on expected exceptions triggered by the rule under test.
 *
 * TODO update other tests that validate rule error handling so that they use this class.
 */
public class SafeJUnitRule implements MethodRule {

    private final MethodRule testedRule;
    private Throwable reportedThrowable;
    private final static ThrowableAssert NO_OP = new ThrowableAssert() { public void doAssert(Throwable throwable) { } };
    private ThrowableAssert throwableAssert = NO_OP;

    /**
     * Wraps rule under test with exception handling so that it is easy to assert on exceptions fired from the tested rule.
     */
    public SafeJUnitRule(MethodRule testedRule) {
        this.testedRule = testedRule;
    }

    public Statement apply(final Statement base, FrameworkMethod method, Object target) {
        return new Statement() {
            public void evaluate() throws Throwable {
                try {
                    base.evaluate();
                } catch (Throwable throwable) {
                    reportedThrowable = throwable;
                    throwableAssert.doAssert(throwable);
                    return;
                }
                if (throwableAssert != NO_OP) {
                    //looks like the user expects a throwable but it was not thrown!
                    throw new ExpectedThrowableNotReported("Expected the tested rule to throw an exception but it did not.");
                }
            }
        };
    }

    /**
     * Expects that _after_ the test, the rule will fire specific exception with specific exception message
     */
    public void expectThrowable(final Class<? extends Throwable> expected, final String expectedMessage) {
        this.expectThrowable(new ThrowableAssert() {
            public void doAssert(Throwable throwable) {
                Assertions.assertThat(throwable)
                        .isInstanceOf(expected)
                        .hasMessage(expectedMessage);
            }
        });
    }

    /**
     * Expects that _after_ the test, the rule will fire an exception
     */
    public void expectThrowable(ThrowableAssert throwableAssert) {
        this.throwableAssert = throwableAssert;
    }

    /**
     * Offers a way to assert the throwable triggered by the tested rule
     */
    public interface ThrowableAssert {
        void doAssert(Throwable throwable);
    }

    /**
     * Rule under test
     */
    public MethodRule getTestedRule() {
        return testedRule;
    }

    /**
     * Exception reported by rule under test. Can be null, meaning the tested rule did not emit an exception.
     */
    public Throwable getReportedThrowable() {
        return reportedThrowable;
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
