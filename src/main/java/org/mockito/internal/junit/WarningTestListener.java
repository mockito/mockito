package org.mockito.internal.junit;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.MockitoLogger;

import java.util.Collection;

class WarningTestListener implements MockitoTestListener {

    private final MockitoLogger logger;
    private String testName;

    WarningTestListener(MockitoLogger logger) {
        this.logger = logger;
    }

    public void beforeTest(Object testClassInstance, String testMethodName) {
        testName = testClassInstance.getClass().getSimpleName() + "." + testMethodName;
        MockitoAnnotations.initMocks(testClassInstance);
    }

    public void afterTest(Collection<Object> mocks, Throwable testFailure) {
        if (testFailure != null) {
            //print stubbing mismatches only when there is a test failure
            //to avoid false negatives. Give hint only when test fails.
            new ArgMismatchFinder().getStubbingArgMismatches(mocks).format(testName, logger);
        } else {
            //Validate only when there is no test failure to avoid reporting multiple problems
            Mockito.validateMockitoUsage();

            //print unused stubbings only when test succeeds to avoid reporting multiple problems and confusing users
            new UnusedStubbingsFinder().getUnusedStubbings(mocks).format(testName, logger);
        }
    }
}
