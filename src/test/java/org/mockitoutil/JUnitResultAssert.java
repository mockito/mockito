package org.mockitoutil;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockitoutil.TestBase.filterLineNo;

/**
 * Assertion utility for cleaner & easier to debug tests that inspect on JUnit's Result object
 */
public class JUnitResultAssert {
    private Result result;

    private JUnitResultAssert(Result result) {
        this.result = result;
    }

    public void isSuccessful() {
        if (result.wasSuccessful()) {
            return;
        }

        StringBuilder sb = new StringBuilder("There were " + result.getFailures().size() + " test failures:\n");
        int count = 0;
        for (Failure f : result.getFailures()) {
            sb.append("  <-----> ").append(++count).append(". ").append(f.getTrace()).append("\n");
        }
        throw new AssertionError(sb.toString());
    }

    /**
     * @param expectedFailureCount - expected number of failures
     * @param expectedException - the exception of each failure
     */
    public JUnitResultAssert fails(int expectedFailureCount, Class expectedException) {
        if (result.getFailures().size() != expectedFailureCount) {
            throw new AssertionError("Wrong number of failures, expected: " + expectedFailureCount + ", actual: " + result.getFailures().size() + "\n" +
                    formatFailures(result.getFailures()));
        }
        for (Failure f : result.getFailures()) {
            if (!expectedException.isInstance(f.getException())) {
                throw new AssertionError("Incorrect failure type, expected: " + expectedException + ", actual: " + f.getException().getClass().getSimpleName() + "\n" +
                        formatFailures(result.getFailures()));
            }
        }
        return this;
    }

    /**
     * Expects single failure with specific exception and exception message.
     * Automatically filters line numbers from exception messages.
     */
    public JUnitResultAssert fails(Class expectedException, String exceptionMessage) {
        fails(1, expectedException);
        Failure f = result.getFailures().iterator().next();
        assertEquals(filterLineNo(exceptionMessage), filterLineNo(f.getException().getMessage()));
        return this;
    }

    /**
     * Expects failure of given test method with given exception
     */
    public JUnitResultAssert fails(String methodName, Class expectedException) {
        for (Failure f : result.getFailures()) {
            if (methodName.equals(f.getDescription().getMethodName()) && expectedException.isInstance(f.getException())) {
                return this;
            }
        }
        throw new AssertionError("Method '" + methodName + "' did not fail with: " + expectedException.getSimpleName()
                + "\n" + formatFailures(result.getFailures()));
    }

    /**
     * Expects given amount of failures, with given exception triggered by given test method
     */
    public JUnitResultAssert fails(int expectedFailureCount, String methodName, Class expectedException) {
        return fails(expectedFailureCount, expectedException)
                .fails(methodName, expectedException);
    }

    public JUnitResultAssert succeeds(int successCount) {
        int i = result.getRunCount() - result.getFailureCount();
        if (i != successCount) {
            throw new AssertionError("Expected " + successCount + " passes but " + i + "/" + result.getRunCount() + " passed.");
        }
        return this;
    }

    private static String formatFailures(List<Failure> failures) {
        if (failures.isEmpty()) {
            return "<no failures>";
        }
        int count = 1;
        StringBuilder out = new StringBuilder("Failures:\n");
        for (Failure f : failures) {
            out.append(count++).append(". ").append(f.getTrace());
        }
        return out.toString();
    }

    /**
     * Clean assertions for JUnit's result object
     */
    public static JUnitResultAssert assertThat(Result result) {
        return new JUnitResultAssert(result);
    }
}
