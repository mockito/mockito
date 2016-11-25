package org.mockito.internal.junit;

import org.mockito.internal.util.MockitoLogger;
import org.mockito.listeners.MockCreationListener;
import org.mockito.mock.MockCreationSettings;

import java.util.LinkedList;
import java.util.List;

class WarningTestListener implements MockitoTestListener, MockCreationListener {

    private final MockitoLogger logger;
    private final List<Object> mocks = new LinkedList<Object>();

    WarningTestListener(MockitoLogger logger) {
        this.logger = logger;
    }

    public void testFinished(TestFinishedEvent event) {
        String testName = event.getTestClassInstance().getClass().getSimpleName() + "." + event.getTestMethodName();
        if (event.getFailure() != null) {
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