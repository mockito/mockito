package org.mockitoutil;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

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
}
