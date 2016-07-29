package org.mockitoutil;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.List;

/**
 * Assertion utility for cleaner & easier to debug tests that inspect on JUnit's Result object
 */
public class JUnitResultAssert {
    private Result result;

    JUnitResultAssert(Result result) {
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
    public void fails(int expectedFailureCount, Class expectedException) {
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
    }

    private static String formatFailures(List<Failure> failures) {
        if (failures.isEmpty()) {
            return "";
        }
        int count = 1;
        StringBuilder out = new StringBuilder("Failures:");
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
