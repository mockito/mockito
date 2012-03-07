package org.mockitousage.testng.utils;

import org.testng.IConfigurationListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * <strong>Not thread-safe</strong> listener that record only failures, either on the test or on a configuration method.
 */
public class FailureRecordingListener implements ITestListener, IConfigurationListener {

    public List<ITestResult> failedTestResults = new ArrayList<ITestResult>();

    public void onTestFailure(ITestResult result) {
        failedTestResults.add(result);
    }

    public void onConfigurationFailure(ITestResult result) {
        failedTestResults.add(result);
    }

    public Throwable lastThrowable() {
        ListIterator<ITestResult> iterator = failedTestResults.listIterator(failedTestResults.size());
        return iterator.hasPrevious() ? iterator.previous().getThrowable() : null;
    }

    public void clear() {
        failedTestResults.clear();
    }

    // don't care bellow


    public void onConfigurationSuccess(ITestResult itr) { }
    public void onConfigurationSkip(ITestResult itr) { }
    public void onTestStart(ITestResult result) { }
    public void onTestSuccess(ITestResult result) { }
    public void onTestSkipped(ITestResult result) { }
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) { }
    public void onStart(ITestContext context) { }
    public void onFinish(ITestContext context) { }
}
