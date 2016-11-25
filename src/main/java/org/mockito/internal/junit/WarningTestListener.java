package org.mockito.internal.junit;

import org.mockito.internal.util.MockitoLogger;
import org.mockito.mock.MockCreationSettings;

import java.util.LinkedList;
import java.util.List;

class WarningTestListener implements MockitoTestListener {

    private final MockitoLogger logger;
    private String testName;
    private final List<Object> mocks = new LinkedList<Object>();

    WarningTestListener(MockitoLogger logger) {
        this.logger = logger;
    }

    public void beforeTest(Object testClassInstance, String testMethodName) {
        //TODO strict - this is not safe, there can be multiple tests starting without finishing (concurrent)
        testName = testClassInstance.getClass().getSimpleName() + "." + testMethodName;
    }

    public void afterTest(Throwable testFailure) {
        if (testFailure != null) {
            //print stubbing mismatches only when there is a test failure
            //to avoid false negatives. Give hint only when test fails.
            new ArgMismatchFinder().getStubbingArgMismatches(mocks).format(testName, logger);
        } else {
            //print unused stubbings only when test succeeds to avoid reporting multiple problems and confusing users
            new UnusedStubbingsFinder().getUnusedStubbings(mocks).format(testName, logger);
        }
    }

    public void onMockCreated(Object mock, MockCreationSettings settings) {
        this.mocks.add(mock);
    }
}